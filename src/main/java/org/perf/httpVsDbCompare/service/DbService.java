package org.perf.httpVsDbCompare.service;

import org.perf.httpVsDbCompare.client.PgClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DbService {
	
	private static final Logger LOGGER = Logger.getLogger(DbService.class.getName());
	
	@Value("${db.batch.size}")
	private Integer batchSize;
	
	@Value("${tenant.id}")
	private String tenantId;
	
	@Autowired
	private PgClient pgClient;
	
	public String estimate(int numRecords) {
		var ids = pgClient.getMarcIds(tenantId, numRecords);
		LOGGER.log(Level.INFO, "Started DB {0} records", numRecords);
		long startTime = System.nanoTime();
		for (var id : ids) {
			var marc = pgClient.getMarcById(tenantId, id);
			LOGGER.log(Level.FINE, "Marc record: {0}", marc);
		}
		var res = String.valueOf((System.nanoTime() - startTime) / 1_000_000_000);
		LOGGER.log(Level.INFO, "Ended DB {0} records", numRecords);
		return res;
	}
	
	public String estimateBatch(int numRecords) {
		var ids = pgClient.getMarcIds(tenantId, numRecords);
		LOGGER.log(Level.INFO, "Started DB batch {0} records", numRecords);
		long startTime = System.nanoTime();
		for (int i = 0; i < ids.size(); i += batchSize) {
			var toIndex = i + batchSize;
			var marc = pgClient.getMarcsByIds(tenantId, ids.subList(i, toIndex >= ids.size() ? ids.size() - 1 : toIndex));
			LOGGER.log(Level.FINE, "Marc records: {0}", marc);
		}
		var res = String.valueOf((System.nanoTime() - startTime) / 1_000_000_000);
		LOGGER.log(Level.INFO, "Ended DB batch {0} records", numRecords);
		return res;
	}
}
