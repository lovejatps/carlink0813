package com.uniits.carlink.action;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.uniits.carlink.pojo.User;
import com.uniits.carlink.service.IUserService;

public class UserAction extends ActionSupport implements ServletResponseAware{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5736291853448400197L;

	public IUserService getUserService() {
		return userService;
	}
	@Resource(name="userService")
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	private IUserService userService;
	private String username;
	private int age ;
	private HttpServletResponse response;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
	public String execute(){
		
		try {
			User user = new User();
			user.setAge(age);
			user.setName(username);
			userService.addUser(user);
			System.out.println(userService.toString());
			PrintWriter pWriter = response.getWriter();
			pWriter.print("Hello World");
			pWriter.flush();
			pWriter.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void setServletResponse(HttpServletResponse resp) {
		response = resp;
	}
}
