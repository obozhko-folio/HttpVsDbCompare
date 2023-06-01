package org.perf.httpVsDbCompare.client;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpClient {
	
	private static final Logger LOGGER = Logger.getLogger(HttpClient.class.getName());
	
	@Value("${http.host}")
	private String host;
	
	@Value("${http.port}")
	private String port;
	
	@Value("${http.username}")
	private String username;
	
	@Value("${http.password}")
	private String password;
	
	@Value("${tenant.id}")
	private String tenant;
	
	private String token;
	
	public String getMarcById(String id) {
		if (token == null) {
			login();
		}
		HttpGet request = new HttpGet(host + ":" + port + "/source-storage/source-records/" + id);
		request.setHeader("x-okapi-token", token);
		request.setHeader("x-okapi-tenant", tenant);
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");
		
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpResponse response = client.execute(request);
			String content = new String(response.getEntity().getContent().readAllBytes());
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//				throw new RuntimeException(tenant + " Failed to get instance: " + response.getStatusLine().getStatusCode() + "\n" + content);
    LOGGER.log(Level.SEVERE, tenant + " Failed to get instance: " + response.getStatusLine().getStatusCode() + "\n" + content);
			 return "";
			} else {
				return new JSONObject(content).getJSONObject("parsedRecord").getJSONObject("content").toString();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<String> getMarcsByIds(List<String> ids) {
		List<String> res = new ArrayList<>();
		if (token == null) {
			login();
		}
		HttpPost request = new HttpPost(host + ":" + port + "/source-storage/source-records");
		request.setHeader("x-okapi-token", token);
		request.setHeader("x-okapi-tenant", tenant);
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");
		StringBuilder jsonEntity = new StringBuilder("[");
		ids.forEach(id -> jsonEntity.append("\"").append(id).append("\"").append(","));
		jsonEntity.setLength(jsonEntity.length() - 1);
		jsonEntity.append("]");
		try {
			request.setEntity(new StringEntity(jsonEntity.toString()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpResponse response = client.execute(request);
			String content = new String(response.getEntity().getContent().readAllBytes());
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException(tenant + " Failed to get instance: " + response.getStatusLine().getStatusCode() + "\n" + content);
			} else {
				new JSONObject(content).getJSONArray("sourceRecords").forEach(json -> {
					res.add(((JSONObject)json).getJSONObject("parsedRecord").getJSONObject("content").toString());
				});
				LOGGER.log(Level.FINE, "Res: {0}", res);
				return res;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void login() {
		HttpPost request = new HttpPost(host + ":" + port + "/authn/login");
		request.setHeader("x-okapi-tenant", tenant);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-Type", "application/json");
		var jsonEntity = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
		try {
			request.setEntity(new StringEntity(jsonEntity));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		try {
			SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
											SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(), NoopHostnameVerifier.INSTANCE);
			try (CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(scsf).build()) {
				HttpResponse response = client.execute(request);
				String content = new String(response.getEntity().getContent().readAllBytes());
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
					throw new RuntimeException(tenant + " Failed: " + response.getStatusLine().getStatusCode() + "\n" + content);
				} else {
					token = new JSONObject(content).getString("okapiToken");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
