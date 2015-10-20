package com.uniits.carlink.action;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.uniits.carlink.service.IWeatherService;
import com.uniits.carlink.utils.Config;
import com.uniits.carlink.utils.StringUtil;

@Controller("oldWeather")
@Scope("prototype")
public class WeatherAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -169095129608230164L;

	
	private String city;
	
	

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Autowired
	private IWeatherService weatherService;

	public String findWeather() throws Exception {
		String ret = "";
		String disposeCity = StringUtil.disposeString(city);
		if (StringUtils.isEmpty(disposeCity)) {
			ret = "" + Config.ReturnCode.PARAM_EXCEPTION;
		} else {
			ret = weatherService.getWeather(disposeCity);
		}
		writeOut(ret);
		return null;
	}

}
