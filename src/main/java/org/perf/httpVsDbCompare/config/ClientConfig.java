package org.perf.httpVsDbCompare.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SocketFactory;
import org.perf.httpVsDbCompare.client.HttpClient;
import org.perf.httpVsDbCompare.client.PgClient;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
public class ClientConfig {
	
	private static final Logger LOGGER = Logger.getLogger(ClientConfig.class.getName());
	
	@Value("${datasource.host}")
	private String host;
	
	@Value("${datasource.dbname}")
	private String dbname;
	
	@Value("${datasource.username}")
	private String username;
	
	@Value("${datasource.sshusername}")
	private String sshUsername;
	
	@Value("${datasource.sshhost}")
	private String sshHost;

	@Value("${datasource.password}")
	private String password;
	
	@Value("${datasource.dbport}")
	private Integer dbPort;
	
	@Value("classpath:ssh/bastio.pem")
	private Resource pkFile;
	
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
//		JSch jsch = new JSch();
		try {
			// ssh
//			jsch.addIdentity(null, pkFile.getInputStream().readAllBytes(), null, null);
//			Session session = jsch.getSession(sshUsername, sshHost, 22);
//			session.setConfig("StrictHostKeyChecking", "no");
//			session.connect();
//			LOGGER.info("Connected to SSH session successfully.");
//			int port = session.setPortForwardingL(5433, host, dbPort);
			
			DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
			dataSourceBuilder.driverClassName("org.postgresql.Driver");
//			dataSourceBuilder.url(String.format("jdbc:postgresql://localhost:%d/%s", 5433, dbname)); // ssh
			dataSourceBuilder.url(String.format("jdbc:postgresql://%s:%d/%s", host, 5432, dbname));
			dataSourceBuilder.username(username);
			dataSourceBuilder.password(password);
			return dataSourceBuilder.build();
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}
}
