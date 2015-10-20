package com.uniits.carlink.dao.impl;


import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.uniits.carlink.dao.IUserDAO;
import com.uniits.carlink.pojo.User;

@Component(value="userDAO")
@Scope(value="prototype")
public class UserDAOImpl implements IUserDAO{
	
	
	private HibernateTemplate hibernateTemplate;
	

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void addUser(User user) throws Exception{
		hibernateTemplate.save(user);
		hibernateTemplate.execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				
				return null;
			}
		});
	}
}
