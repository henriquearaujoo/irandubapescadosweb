package com.irandubamodulo01.bean;

import com.irandubamodulo01.dao.ArquivoDAO;
import com.irandubamodulo01.dao.PedidoDAO;
import com.irandubamodulo01.enumerated.StatusPedido;
import com.irandubamodulo01.model.Arquivo;
import com.irandubamodulo01.model.Pedido;
import com.irandubamodulo01.model.TipoPagamento;
import com.irandubamodulo01.util.Filtro;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named(value = "pedidosBean")
@ViewScoped
public class PedidosBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private @Inject Pedido pedido;
	private @Inject Arquivo arquivo;
	private @Inject PedidoDAO pedidoDAO;
	private @Inject ArquivoDAO arquivoDAO;

	private Filtro filtro = new Filtro();
	private LazyDataModel<Pedido> model;
	private StreamedContent fileDownload;
	private Part notaFiscal;



	public PedidosBean() {
		carregarPedidos();
	}

	public void carregarPedidos() {

		model = new LazyDataModel<Pedido>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<Pedido> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				
				setRowCount(pedidoDAO.quantidadeFiltrados(filtro));
				return pedidoDAO.filtrados(filtro);
			}

		};
	}
	
	public void carregarDetalhes(){
		
		List<Arquivo> arquivos = arquivoDAO.getArquivosPorPedido(getPedido());
		
		if (arquivos.size() > 0)
    		arquivo = arquivos.get(0);
		else
			arquivo = null;
	}
	
	public void salvarNotaFiscal() {

		if(getNotaFiscal() != null) {
			String fileName = getFileName(getNotaFiscal());
			try {

				List<Arquivo> list = arquivoDAO.getArquivosPorPedido(getPedido());
				Arquivo arquivo1 = null;
				if (list != null && list.size() > 0) {
					arquivo1 = list.get(0);
				} else
					arquivo1 = new Arquivo();

				arquivo1.setPedido(getPedido());
				arquivo1.setNome(fileName);
				arquivo1.setTamanho(getNotaFiscal().getSize());
				arquivo1.setTipo(getNotaFiscal().getContentType());
				arquivo1.setDados(IOUtils.toByteArray(getNotaFiscal().getInputStream()));

				arquivoDAO.save(arquivo1);

				addMessage("Nota fiscal adicionada com sucesso.", "", FacesMessage.SEVERITY_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				addMessage("Não foi possivel adicionar a nota fiscal.", "", FacesMessage.SEVERITY_INFO);
			}
		}
    }

	private String getFileName(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;
	}
		
	public void downloadComprovante(){

		try{

			if (arquivo != null){
				arquivo = arquivoDAO.getById(Arquivo.class, arquivo.getId());

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + arquivo.getNome() + "\"");
				response.getOutputStream().write(arquivo.getDados());
				response.getOutputStream().flush();
				response.getOutputStream().close();
				FacesContext.getCurrentInstance().responseComplete();
			}else{
				addMessage("Aviso: ", "Esse pedido não possui nota fiscal.", FacesMessage.SEVERITY_WARN);
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		
		arquivo.setDados(null);
		
	}
	
	public void finalizarPedido(){
		
		try {
			getPedido().setStatus(StatusPedido.FINALIZADO);
			
			pedidoDAO.save(getPedido());

			carregarPedidos();
			
			addMessage("Informação: ", "Pedido finalizado com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("Erro: ", "Não foi possivel finalizar o pedido.", FacesMessage.SEVERITY_ERROR);
		}
	}

	public void limparFiltro() {
		filtro.setNome("");
		filtro.setDataInicio(null);
		filtro.setDataFinal(null);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public StatusPedido[] getStatus(){
		return StatusPedido.values();
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public LazyDataModel<Pedido> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Pedido> model) {
		this.model = model;
	}

	public StreamedContent getFileDownload() {
		return fileDownload;
	}

	public void setFileDownload(StreamedContent fileDownload) {
		this.fileDownload = fileDownload;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Part getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(Part notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
}
