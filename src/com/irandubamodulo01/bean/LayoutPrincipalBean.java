package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


@ManagedBean(name="layoutBean")
@ViewScoped
public class LayoutPrincipalBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean tipoExport = false;
	

	public LayoutPrincipalBean(){}
	
	public void postProcessXLS(Object document){
		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFDataFormat dataFormat = wb.createDataFormat();
		cellStyle.setDataFormat(dataFormat.getFormat("0.00"));
		
		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell(6);
			
			String valor = cell.getStringCellValue();
			
			//cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
		    //cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		    
			BigDecimal dbVal = new BigDecimal(valor.replace(",", "."));
			dbVal.setScale(2, BigDecimal.ROUND_HALF_UP);
			cell.setCellValue(dbVal.doubleValue());
			
			cell.setCellStyle(cellStyle);			

		}
	}
	
	
	
	public Boolean getTipoExport() {
		return tipoExport;
	}



	public void setTipoExport(Boolean tipoExport) {
		this.tipoExport = tipoExport;
	}



	public String iniciarTelaUsuarios(){
		return "usuarios?faces-redirect=true";
	}
	
	public String iniciarTelaBaixaCompras(){
		return "baixa_compra?faces-redirect=true";
	}

	public String iniciarTelaEstoque(){
		return "estoque?faces-redirect=true";
	}

	public String iniciarTelaArmazenamento(){
		return "armazenamento?faces-redirect=true";
	}
	
	public String iniciarTelaRetirada(){
		return "retirada?faces-redirect=true";
	}
	
	public String iniciarTelaPeixes(){
		return "peixes?faces-redirect=true";
	}
	
	public String iniciarTelaTipoPeixe(){
		return "tipo_peixe?faces-redirect=true";
	}
	
	public String iniciarTelaTamanhoPeixe(){
		return "tamanho_peixe?faces-redirect=true";
	}
	
	public String iniciarTelaVendedor(){
		return "vendedores?faces-redirect=true";
	}

	public String iniciarTelaCliente(){
		return "clientes?faces-redirect=true";
	}

	public String iniciarTelaTransportadora(){
		return "transportadoras?faces-redirect=true";
	}

	public String iniciarTelaEmbalagem(){
		return "embalagem?faces-redirect=true";
	}
	
	public String iniciarTelaCamaras(){
		return "camaras?faces-redirect=true";
	}

	public String iniciarTelaFornec(){
		return "fornecedores?faces-redirect=true";
	}
	
	public String iniciarTelaRelatoriCompras(){
		return "relatorio_compras?faces-redirect=true";
	}

	public String iniciarTelaRelatorioPagamentosPescado(){
		return "relatorio_pagamentos_pescado?faces-redirect=true";
	}
	
	public String iniciarTelaRelatorioHistoricoCompras(){
		return "relatorio_historico_compra?faces-redirect=true";
	}

	public String iniciarTelaRelatorioCompraDetalhada(){
		return "relatorio_compras_detalhadas?faces-redirect=true";
	}

	public String iniciarTelaRelatorioPerdaArmazenamento(){
		return "relatorio_perda_armazenamento?faces-redirect=true";
	}

	public String iniciarTelaRelatorioArmazenamento(){
		return "relatorio_armazenamento?faces-redirect=true";
	}
	public String iniciarTelaComprasFinalizadas(){
		return "compras_finalizadas?faces-redirect=true";
	}
	
	public String iniciarDetalhesCompra(){
		return "detalhes_compra_temp?faces-redirect=true";
	}
	
	public String iniciarTelaPagamentosPendentes(){
		return "pagamentos_pendentes?faces-redirect=true";
	}

	public String iniciarTelaPedido(){
		return "pedidos?faces-redirect=true";
	}
	
	public String iniciarTelaRastreabilidade(){
		return "rastreabilidade?faces-redirect=true";
	}
}
