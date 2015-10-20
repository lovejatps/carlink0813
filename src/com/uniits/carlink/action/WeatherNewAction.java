package com.uniits.carlink.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.uniits.carlink.service.IWeatherNewService;
import com.uniits.carlink.service.IWeatherService;
import com.uniits.carlink.utils.Config;
import com.uniits.carlink.utils.NewStringUtil;
import com.uniits.carlink.utils.StringUtil;

@Controller("weather")
@Scope("prototype")
public class WeatherNewAction extends BaseAction {

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
	private IWeatherNewService weatherNewService;

	public String findWeather() throws Exception {
		String ret = "";
		//String disposeCity = NewStringUtil.disposeString(city);
		System.out.println("city::"+city);

		
		if("ZZZZ".equals(city)){
			return null;
		}
		
		if (StringUtils.isEmpty(city)) {
			ret = "" + Config.ReturnCode.PARAM_EXCEPTION;
		} else {
			ret = weatherNewService.getWeather(city);
		}
		writeOut(ret);
		return null;
	}

		

}
