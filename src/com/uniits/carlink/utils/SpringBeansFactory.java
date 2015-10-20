package com.uniits.carlink.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class SpringBeansFactory implements BeanFactoryAware {

	private static BeanFactory beanFactory = null;  
	   
    private static SpringBeansFactory springBeansFactory = null;  
   
    public void setBeanFactory(BeanFactory factory) throws BeansException {  
        this.beanFactory = factory;  
    }  
    
    public static SpringBeansFactory getBeansFactory() {
    	if(springBeansFactory == null) {
    		springBeansFactory =  (SpringBeansFactory) beanFactory.getBean("beansFactory");
    	}
    	return springBeansFactory;
    }
    
    public Object getBean(String beanName) {
    	return beanFactory.getBean(beanName);
    }
}
