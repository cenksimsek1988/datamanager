package com.softactive.core.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthFilter implements Filter {

	@Override
	public void destroy() {
		System.out.println("");
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		 HttpServletResponse res = (HttpServletResponse) response;
         res.sendRedirect(req.getContextPath()
                 + "/pages/start.xhtml");
		
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("");
		
	}

}
