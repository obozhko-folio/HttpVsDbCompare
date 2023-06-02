package org.perf.httpVsDbCompare.service;

import org.perf.httpVsDbCompare.client.HttpClient;
import org.perf.httpVsDbCompare.client.PgClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpService {
	
	private static final Logger LOGGER = Logger.getLogger(HttpService.class.getName());
	
	@Value("${http.batch.size}")
	private Integer batchSize;
	
	@Value("${tenant.id}")
	private String tenant;
	
	@Autowired
	private PgClient pgClient;
	
	@Autowired
	private HttpClient httpClient;
	
	public String estimate(int numRecords) {
		var ids = pgClient.getMarcIds(tenant, numRecords);
		LOGGER.log(Level.INFO, "Started HTTP {0} records", numRecords);
		long startTime = System.nanoTime();
		for (var id : ids) {
			var marc = httpClient.getMarcById(id);
			LOGGER.log(Level.FINE, "Marc record: {0}", marc);
		}
		var res = String.valueOf((System.nanoTime() - startTime) / 1_000_000_000);
		LOGGER.log(Level.INFO, "Ended HTTP {0} records", numRecords);
		return res;
	}
	
	public String estimateBatch(int numRecords) {
		var ids = pgClient.getMarcIds(tenant, numRecords);
		LOGGER.log(Level.INFO, "Started HTTP batch {0} records", numRecords);
		long startTime = System.nanoTime();
		for (int i = 0; i < ids.size(); i += batchSize) {
			var toIndex = i + batchSize;
			var marc = httpClient.getMarcsByIds(ids.subList(i, toIndex >= ids.size() ? ids.size() - 1 : toIndex));
			LOGGER.log(Level.FINE, "Marc record batch: {0}", marc);
		}
		var res = String.valueOf((System.nanoTime() - startTime) / 1_000_000_000);
		LOGGER.log(Level.INFO, "Ended HTTP batch {0} records", numRecords);
		return res;
	}
	
}
