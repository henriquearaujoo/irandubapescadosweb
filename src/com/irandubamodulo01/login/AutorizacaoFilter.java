package com.irandubamodulo01.login;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.irandubamodulo01.bean.UsuarioSessionBean;


public class AutorizacaoFilter implements Filter{

	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		/*HttpSession session = ((HttpServletRequest)request).getSession(false);

		if (session != null && !session.isNew()){
			chain.doFilter(request, response);
		} else {
			((HttpServletResponse)response).sendRedirect(((HttpServletRequest)request).getContextPath() + "/main/login.xhtml");
		}*/

		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest  req = (HttpServletRequest) request;
		HttpSession httpSession = req.getSession(false);
		if ((httpSession == null || httpSession.isNew())
				&& !req.getRequestURI().endsWith("/login.xhtml")
				&& !req.getRequestURI().contains("/javax.faces.resource/")) {
			resp.sendRedirect(req.getContextPath()+ "/main/login.xhtml");
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
