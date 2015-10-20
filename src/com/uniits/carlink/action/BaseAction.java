/**
 * 
 */
package com.uniits.carlink.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Cleanup;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author lcw
 * @since 2015-2-10 下午5:04:45
 */
public class BaseAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 263478482508778880L;

	//-- 取得Request/Response/Session的简化函数 --//
	/**
	 * 取得HttpSession的简化函数.
	 */
	public static HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	/**
	 * 取得HttpSession的简化函数.
	 */
	public static HttpSession getSession(boolean isNew) {
		return ServletActionContext.getRequest().getSession(isNew);
	}

	/**
	 * 取得HttpSession中Attribute的简化函数.
	 */
	public static Object getSessionAttribute(String name) {
		HttpSession session = getSession(false);
		return (session != null ? session.getAttribute(name) : null);
	}

	/**
	 * 取得HttpRequest的简化函数.
	 */
	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 取得HttpRequest中Parameter的简化方法.
	 */
	public static String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	/**
	 * 取得HttpResponse的简化函数.
	 */
	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * 直接输出字符串
	 * @param result
	 * @throws IOException
	 */
	public void writeOut(String result) throws IOException {
		getResponse().setContentType("text/html; charset=UTF-8");
		@Cleanup
		PrintWriter pWriter = getResponse().getWriter();
		pWriter.write(result);
		pWriter.flush();
	}

}
