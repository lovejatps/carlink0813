package com.uniits.carlink.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_user")
@SequenceGenerator(name = "SEQ_USER_ID", sequenceName = "SEQ_USER_ID", allocationSize = 1)
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3674946417260777402L;

	private int userId;
	private String name;
	private int age;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_ID")
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
