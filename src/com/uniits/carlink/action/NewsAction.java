package com.uniits.carlink.action;

import java.io.IOException;
import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.uniits.carlink.service.INewsService;

@Controller("news")
@Scope("prototype")
public class NewsAction extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1908025706133198579L;
	
	
	private String keyword;
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Autowired
	private INewsService newsService;
	
	public String findNews() throws IOException {
		writeOut(newsService.getNewsByKeyword(keyword));
		return null;
	}
	
}
