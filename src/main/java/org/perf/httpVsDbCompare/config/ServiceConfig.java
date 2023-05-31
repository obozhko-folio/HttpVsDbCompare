package org.perf.httpVsDbCompare.config;

import org.perf.httpVsDbCompare.client.PgClient;
import org.perf.httpVsDbCompare.service.DbService;
import org.perf.httpVsDbCompare.service.HttpService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

	@Bean
	public HttpService httpService() {
		return new HttpService();
	}
	
	@Bean
	public DbService dbService() {
		return new DbService();
	}
}
