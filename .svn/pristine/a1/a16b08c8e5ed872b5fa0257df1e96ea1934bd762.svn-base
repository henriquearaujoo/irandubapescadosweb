package com.irandubamodulo01.util;

import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

public class ReportUtil {
	
	/**
     * Abre um relatório usando um datasource genérico.
     *
     * @param titulo Título usado na janela do relatório.
     * @param inputStream InputStream que contém o relatório.
     * @param parametros Parâmetros utilizados pelo relatório.
     * @param dataSource Datasource a ser utilizado pelo relatório.
     * 
     * @throws JRException Caso ocorra algum problema na execução do relatório
	 * @throws IOException 
     */
    public static void openReport(
            String titulo,
            String nome,
            JasperReport report,
            Map parametros,
            JRDataSource dataSource ) throws JRException, IOException {
 
    	FacesContext fctx = FacesContext.getCurrentInstance();

		HttpServletResponse resp = (HttpServletResponse) fctx.getExternalContext().getResponse();

		resp.setContentType( "application/pdf" );
		resp.setHeader( "Content-Disposition", "atachment;filename=" + nome + ".pdf" );

		ServletOutputStream out = resp.getOutputStream();
		
        /*
         * Cria um JasperPrint, que é a versão preenchida do relatório,
         * usando um datasource genérico.
         */
        JasperPrint print = JasperFillManager.fillReport(
                report, parametros, dataSource );
 
        // abre o JasperPrint em um JFrame
        //viewReportFrame( titulo, print );
        
        //JasperViewer.viewReport(print, false);
        
        //JasperExportManager.exportReportToPdfStream(print, resp.getOutputStream());
        
        byte[] pdfByteArray = JasperExportManager.exportReportToPdf(print);
        resp.setContentLength( pdfByteArray.length );
        
        out.write(pdfByteArray, 0, pdfByteArray.length);
        out.flush();
        out.close();
        
        fctx.responseComplete();
        
    }
    
    public static void openReportCon(
            String titulo,
            String nome,
            JasperReport report,
            Map parametros,
            Connection conn) throws JRException, IOException {
 
    	FacesContext fctx = FacesContext.getCurrentInstance();

		HttpServletResponse resp = (HttpServletResponse) fctx.getExternalContext().getResponse();

		resp.setContentType( "application/pdf" );
		resp.setHeader( "Content-Disposition", "atachment;filename=" + nome + ".pdf" );

		ServletOutputStream out = resp.getOutputStream();
		
        /*
         * Cria um JasperPrint, que é a versão preenchida do relatório,
         * usando um datasource genérico.
         */
        JasperPrint print = JasperFillManager.fillReport(
                report, parametros, conn );
 
        // abre o JasperPrint em um JFrame
        //viewReportFrame( titulo, print );
        
        //JasperViewer.viewReport(print, false);
        
        //JasperExportManager.exportReportToPdfStream(print, resp.getOutputStream());
        
        byte[] pdfByteArray = JasperExportManager.exportReportToPdf(print);
        resp.setContentLength( pdfByteArray.length );
        
        out.write(pdfByteArray, 0, pdfByteArray.length);
        out.flush();
        out.close();
        
        fctx.responseComplete();
        
    }
    
	 /**
     * Cria um JFrame para exibir o relatório representado pelo JasperPrint.
     *
     * @param titulo Título do JFrame.
     * @param print JasperPrint do relatório.
     */
    private static void viewReportFrame( String titulo, JasperPrint print ) {
 
        /*
         * Cria um JRViewer para exibir o relatório.
         * Um JRViewer é uma JPanel.
         */
        JRViewer viewer = new JRViewer( print );
 
        // cria o JFrame
        JFrame frameRelatorio = new JFrame( titulo );
 
        // adiciona o JRViewer no JFrame
        frameRelatorio.add( viewer, BorderLayout.CENTER );
 
        // configura o tamanho padrão do JFrame
        frameRelatorio.setSize( 500, 500 );
 
        // maximiza o JFrame para ocupar a tela toda.
        frameRelatorio.setExtendedState( JFrame.MAXIMIZED_BOTH );
 
        // configura a operação padrão quando o JFrame for fechado.
        frameRelatorio.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
 
        // exibe o JFrame
        frameRelatorio.setVisible( true );
 
    }
}
