/**
 * 
 */
package com.adaptor.uni.db;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adaptor.uni.App;
import com.adaptor.uni.utils.TAdaptorConstants;

public class TDatabaseConn {
	
	private static TDatabaseConn connection;
	
	/**
	 * Logger for logging  events
	 * */
	public static  final Logger logger = LogManager.getLogger(TDatabaseConn.class);
	
	private Connection dbConn = null;
	
	private TDatabaseConn() {
		dbConn = createConnection();
	}
	
	public static TDatabaseConn getInstance() {
		connection = new TDatabaseConn();
		return connection;
	}

	private Connection createConnection() {

		try {
			Class.forName(TAdaptorConstants.JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			logger.error("MySQL driver problem occured.",e);
			return null;
		}

		Connection connection = null;

		try {
			connection = DriverManager
					.getConnection(TAdaptorConstants.DB_URL,TAdaptorConstants.DB_USER, 
							TAdaptorConstants.DB_PASS);

		} catch (SQLException e) {
			logger.error("Connection Failed[ " + TAdaptorConstants.DB_URL + " ]",e);
			return null;
		}

		if (connection != null) {
			logger.info("Successfully connected to DB");
			return connection;
		} else {
			logger.error("Failed to make connection!");
			return null;
		}
	}
	
	
	public Connection getConnection() {
		return dbConn;
	}
}