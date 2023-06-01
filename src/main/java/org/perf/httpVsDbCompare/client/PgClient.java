package org.perf.httpVsDbCompare.client;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PgClient {
	
	private static final Logger LOGGER = Logger.getLogger(PgClient.class.getName());
	
	@Autowired
	private DataSource dataSource;
	
	public String getMarcById(String tenantId, String id) {
		try {
			try (var conn = dataSource.getConnection();
								Statement stmt = conn.createStatement();
								ResultSet rs = stmt.executeQuery(
																String.format("SELECT mlb.content FROM %s_mod_source_record_storage.marc_records_lb mlb\n" +
																"JOIN %s_mod_source_record_storage.records_lb lb\n" +
																"ON mlb.id = lb.id WHERE lb.id = '%s'", tenantId, tenantId, id))) {
				if (rs.next()) {
					var next = rs.getString(1);
					JSONObject json = new JSONObject(next);
					return json.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getMarcsByIds(String tenantId, List<String> ids) {
		List<String> res = new ArrayList<>();
		StringBuilder condition = new StringBuilder();
		ids.forEach(id -> condition.append("lb.id = '").append(id).append("'").append(" OR "));
		condition.setLength(condition.length() - 4);
		var query = String.format("SELECT mlb.content FROM %s_mod_source_record_storage.marc_records_lb mlb\n" +
										"JOIN %s_mod_source_record_storage.records_lb lb\n" +
										"ON mlb.id = lb.id WHERE %s", tenantId, tenantId, condition);
		LOGGER.log(Level.FINE, "Query: {0}", query);
		try {
			try (var conn = dataSource.getConnection();
								Statement stmt = conn.createStatement();
								ResultSet rs = stmt.executeQuery(query)) {
				while (rs.next()) {
					var next = rs.getString(1);
					JSONObject json = new JSONObject(next);
					res.add(json.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public List<String> getMarcIds(String tenantId, int limit) {
		List<String> res = new ArrayList<>();
		try {
			
			try (var conn = dataSource.getConnection();
								Statement stmt = conn.createStatement();
								ResultSet rs = stmt.executeQuery(
																String.format("SELECT lb.id FROM %s_mod_source_record_storage.records_lb lb WHERE lb.record_type = 'MARC_BIB' AND lb.state = 'ACTUAL' LIMIT %d", tenantId, limit))) {
				while (rs.next()) {
					var next = rs.getString(1);
					res.add(next);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
