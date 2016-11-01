package com.irandubamodulo01.bean;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import com.irandubamodulo01.dao.ArquivoDAO;
import com.irandubamodulo01.enumerated.TipoCadastro;
import com.irandubamodulo01.model.Arquivo;
import com.irandubamodulo01.model.Cliente;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.irandubamodulo01.dao.PeixeDAO;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.util.Filtro;
import com.lowagie.text.pdf.codec.Base64.InputStream;

@Named(value = "peixeBean")
@ViewScoped
public class PeixeBean implements Serializable {

	private @Inject Peixe peixe;
	private @Inject PeixeDAO pxDAO;
	private @Inject ArquivoDAO arquivoDAO;
	private LazyDataModel<Peixe> model;
	private Filtro filtro = new Filtro();
	private UploadedFile file;
	private StreamedContent foto;
	private Part arquivo;
	private Boolean inclusaoContinua = true;
	

	public PeixeBean() {
		model = new LazyDataModel<Peixe>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<Peixe> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				setRowCount(pxDAO.quantidadeFiltrados(filtro));
				return pxDAO.filtrados(filtro);
			}

		};

	}

	public String cancelar() {
		return "peixes?faces-redirect=true";
	}

	public LazyDataModel<Peixe> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Peixe> model) {
		this.model = model;
	}

	public String salvarPeixe() {
		try {

			peixe = pxDAO.save(peixe);
			if (arquivo != null)
				salvarFoto();
			peixe = new Peixe();
			addMessage("Informação: ", "Peixe salvo com sucesso.",
					FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			addMessage("Erro", "Não foi possível salvar o peixe.",
					FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}

		if (getInclusaoContinua()) {
			arquivo = null;
			peixe = new Peixe();
			return "";
		}else
			return cancelar();
	}

	public void deletarPeixe() {
		if (pxDAO.verificarVendasEmProduto(peixe)) {
			addMessage("Não foi possivel excluir", "" + peixe.getDescricao()
					+ " está associado a um lote", FacesMessage.SEVERITY_ERROR);
			peixe = new Peixe();
			return;
		}
		try{
			pxDAO.remove(peixe);
			addMessage("Informação", "Peixe excluído com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível excluir o peixe.", FacesMessage.SEVERITY_ERROR);
		}
	}

	public String editarPeixe() {
		return "peixescad?faces-redirect=true&amp;includeViewParams=true";
	}

	public String prepararTelaCad() {
		peixe = new Peixe();
		return "peixescad?faces-redirect=true";
	}

	public String salvarFoto(){
		// Extract file name from content-disposition header of file part
		String fileName = getFileName(arquivo);
		fileName = "peixe_" + peixe.getId() + "_" + fileName;

		String basePath = "C:" + File.separator + "Iranduba Pescados" + File.separator + "arquivos" + File.separator + "imagens" + File.separator;
		File outputFilePath = new File(basePath + fileName);

		// Copy uploaded file to destination path
		java.io.InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = arquivo.getInputStream();
			outputStream = new FileOutputStream(outputFilePath);

			int read = 0;
			final byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			List<Arquivo> list = arquivoDAO.getArquivosPorPeixe(peixe);
			Arquivo arquivo1 = null;
			if (list != null && list.size() > 0) {
				arquivo1 = list.get(0);

				File fotoAnterior = new File(basePath + arquivo1.getNome());
				if (fotoAnterior.exists())
					fotoAnterior.delete();
			}else
				arquivo1 = new Arquivo();

			arquivo1.setPeixe(peixe);
			arquivo1.setNome(fileName);
			arquivo1.setTamanho(arquivo.getSize());
			arquivo1.setTipo(arquivo.getContentType());

			arquivoDAO.save(arquivo1);

			addMessage("Foto adicionada com sucesso.", "", FacesMessage.SEVERITY_INFO);
		} catch (IOException e) {
			e.printStackTrace();
			addMessage("Não foi possivel adicionar a foto.", "", FacesMessage.SEVERITY_ERROR);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return null;
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

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public TipoCadastro[] getTiposCadastroPeixe(){
		return TipoCadastro.values();
	}

	public Peixe getPeixe() {
		return peixe;
	}

	public void setPeixe(Peixe peixe) {
		this.peixe = peixe;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	

	public void setFoto(StreamedContent foto) {
		this.foto = foto;
	}

	public StreamedContent getFoto() {
		return foto;
	}

	public Part getArquivo() {
		return arquivo;
	}

	public void setArquivo(Part arquivo) {
		this.arquivo = arquivo;
	}

	public Boolean getInclusaoContinua() {
		return inclusaoContinua;
	}

	public void setInclusaoContinua(Boolean inclusaoContinua) {
		this.inclusaoContinua = inclusaoContinua;
	}
}
