package com.irandubamodulo01.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.irandubamodulo01.dao.ArquivoDAO;
import com.irandubamodulo01.model.Arquivo;
import org.apache.commons.io.IOUtils;

import com.irandubamodulo01.dao.PeixeDAO;
import com.irandubamodulo01.model.Peixe;

/**
 * Servlet implementation class FotoServlet
 */
@WebServlet(urlPatterns =  "/foto")
public class FotoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    
	@Inject
	private PeixeDAO dao;

	@Inject
	private ArquivoDAO arquivoDAO;
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!request.getParameter("produto").equalsIgnoreCase("")) {
			Long idProduto = Long.valueOf(request.getParameter("produto"));
			Peixe peixe = new Peixe();
			peixe.setId(idProduto);
			List<Arquivo> arquivos =  arquivoDAO.getArquivosPorPeixe(peixe);
			if(arquivos != null && arquivos.size() > 0) {
				Arquivo arquivo = arquivos.get(0);
				response.setContentType(arquivo.getTipo());
				File file = new File("C:" + File.separator + "Iranduba Pescados" + File.separator + "arquivos" + File.separator + "imagens" + File.separator + arquivo.getNome());
				IOUtils.write(IOUtils.toByteArray(new FileInputStream(file)), response.getOutputStream());
			}
		}
		
	}

}
