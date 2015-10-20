/**
 * 
 */
package com.uniits.carlink.test;

import org.junit.Assert;

import com.uniits.carlink.dao.IUserDAO;

/**
 * @author lcw
 *
 */
public class DbTest extends BaseTest{

	public void testDB() {
		IUserDAO userDao = (IUserDAO) getBean(IUserDAO.class);
		Assert.assertNotNull(userDao);
	}
}
