package org.perf.httpVsDbCompare.config;

import org.perf.httpVsDbCompare.client.HttpClient;
import org.perf.httpVsDbCompare.client.PgClient;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ClientConfig {
	
	@Value("${datasource.host}")
	private String host;
	
	@Value("${datasource.dbname}")
	private String dbname;
	
	@Value("${datasource.username}")
	private String username;

	@Value("${datasource.password}")
	private String password;
	
	@Bean
	public PgClient pgClient() {
		return new PgClient();
	}
	
	@Bean
	public HttpClient httpClient() {
		return new HttpClient();
	}
	
	@Bean
	public DataSource dataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.postgresql.Driver");
		dataSourceBuilder.url(String.format("jdbc:postgresql://%s:5432/%s", host, dbname));
		dataSourceBuilder.username(username);
		dataSourceBuilder.password(password);
		return dataSourceBuilder.build();
	}
}
