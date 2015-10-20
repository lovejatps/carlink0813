package com.uniits.carlink.service.impl;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uniits.carlink.service.INewsService;
import com.uniits.carlink.utils.Config;
import com.uniits.carlink.utils.NewsAssist;

@Component(value="newsService")
@Scope(value="prototype")
public class NewsServiceImpl implements INewsService{
	
	private NewsAssist newsAssist ;
	
	public NewsAssist getNewsAssist() {
		return newsAssist;
	}

	@Resource(name="newsAssist")
	public void setNewsAssist(NewsAssist newsAssist) {
		this.newsAssist = newsAssist;
	}

	public String getNewsByKeyword(String keyword) {
		StringBuffer sbuff = new StringBuffer();
		try {
			sbuff.append(newsAssist.getNewsByKeyWord(keyword));
		} catch (UnsupportedEncodingException e) {
			sbuff.append(Config.error);
			e.printStackTrace();
		}
		return sbuff.toString();
	}

}
