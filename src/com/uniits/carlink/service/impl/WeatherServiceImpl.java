package com.uniits.carlink.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uniits.carlink.service.IWeatherService;
import com.uniits.carlink.utils.Config;
import com.uniits.carlink.utils.WeatherAssist;

@Component(value = "weatherService")
@Scope(value = "prototype")
public class WeatherServiceImpl implements IWeatherService {

	@SuppressWarnings("finally")
	public String getWeather(String district) {
		long startTime = System.currentTimeMillis();
		StringBuffer sBuffer = new StringBuffer();
		try {
			sBuffer.append(WeatherAssist.getInstance().getCityWeatherInfo(district));
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
