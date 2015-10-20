package com.uniits.carlink.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

public class EncodeInterceptor extends StrutsPrepareAndExecuteFilter implements
		Filter {
	private FilterConfig filterconfig;
	private String encoding = null;

	public void destroy() {

	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		if (arg0.getCharacterEncoding() == null) {
			String encod = this.getEncoding();
			if (encod != null) {
				arg0.setCharacterEncoding(encod);
			}
		}
		arg1.setCharacterEncoding(encoding);
		arg2.doFilter(arg0, arg1);
	}

	public void init(FilterConfig arg0) throws ServletException {
		this.filterconfig = arg0;
		this.encoding = this.filterconfig.getInitParameter("encoding");

	}

	public String getEncoding() {
		return this.encoding;
	}
}
