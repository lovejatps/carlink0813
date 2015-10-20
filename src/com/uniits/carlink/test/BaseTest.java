package com.uniits.carlink.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BaseTest extends TestCase {

	ApplicationContext ctx = null;

	 @Override
	protected void setUp() throws Exception {
		 super.setUp();
		String[] xmls = { "classpath:applicationContext.xml" };
		ctx = new FileSystemXmlApplicationContext(xmls);
		System.out.println("-----------配置加载成功--------------");
	}

	/**
	 * 根据beanName得到bean
	 * 
	 * @param beanName
	 * @return
	 */
	public Object getBean(String beanName) {
		if (beanName == null) {
			System.out.println("beanName 不能为空！");
		}
		Object object = ctx.getBean(beanName);
		return object;
	}

	/**
	 * 根据bean类型得到bean
	 * 
	 * @param clzz
	 * @return
	 */
	public Object getBean(Class<?> clzz) {
		Assert.assertNotNull(clzz);
		return ctx.getBean(clzz);
	}

}
