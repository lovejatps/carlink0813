package com.uniits.carlink.logs;

import org.elasticsearch.common.jackson.dataformat.yaml.snakeyaml.util.UriEncoder;

public class XxLogsBefore {

	public void before() {
		System.out.println("before----------------");
	}
	
	public static void main(String[] args) {
		System.out.println(UriEncoder.encode("ba2366_SmartWeatherAPI_82e688d"));
	}
}
