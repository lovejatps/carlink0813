package com.uniits.carlink.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uniits.carlink.service.IWeatherNewService;
import com.uniits.carlink.utils.Config;
import com.uniits.carlink.utils.WeatherNewAssist;

@Component(value = "weatherNewService")
@Scope(value = "prototype")
public class WeatherNewServiceImpl implements IWeatherNewService {
	
	@SuppressWarnings("finally")
	public String getWeather(String district) {
		long startTime = System.currentTimeMillis();
		StringBuffer sBuffer = new StringBuffer();
		try {
			sBuffer.append(WeatherNewAssist.getInstance().getCityWeatherInfo(district));
			long endTime = System.currentTimeMillis();
			System.out.println(endTime - startTime);
		} catch (Exception e) {
			sBuffer.append(Config.error);
			e.printStackTrace();
		} finally {
			return sBuffer.toString();
		}
	}
}
