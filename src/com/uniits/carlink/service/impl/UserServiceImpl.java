package com.uniits.carlink.service.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uniits.carlink.dao.IUserDAO;
import com.uniits.carlink.pojo.User;
import com.uniits.carlink.service.IUserService;

@Component(value="userService")
@Scope(value="prototype")
public class UserServiceImpl implements IUserService{

	private IUserDAO userDAO ;
	
	public IUserDAO getUserDAO() {
		return userDAO;
	}
	@Resource(name="userDAO")
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
		
	}

	public void addUser(User user) throws Exception{
		userDAO.addUser(user);
	}
}
