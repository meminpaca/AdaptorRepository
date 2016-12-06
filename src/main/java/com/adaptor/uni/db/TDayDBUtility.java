/**
 * 
 */
package com.adaptor.uni.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adaptor.uni.App;

/**
 * @author emin.paca
 *
 */
public class TDayDBUtility {
	
	private static List<TDay> days = null;
	/**
	 * Logger for logging  events
	 * */
	public static  final Logger logger = LogManager.getLogger(TDayDBUtility.class);
	

	private static final String daysSQL = "SELECT * FROM Day";
	
	public static List<TDay> getDays() {
		if (days == null) {
			boolean result;
			try {
				result = fillDaysFromDB();
				
				if (result) {
					return days;
				} else {
					logger.error("Failed to fill days from database.");
					return null;
				}
				
			} catch (SQLException e) {
				logger.error("SQL exception occured and failed to fill days from database.");
				return null;
			}
		} else {
			return days;
		}
	}


	/**
	 * @return
	 * @throws SQLException 
	 */
	private static boolean fillDaysFromDB() throws SQLException {
		TDatabaseConn instance = TDatabaseConn.getInstance();
		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and getting days cannot be done.");
		}
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		
		boolean result = false;
		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(daysSQL);

			// execute insert SQL stetement
			ResultSet resultSet = preparedStatement.executeQuery();
			days = new LinkedList<TDay>();
			while (resultSet.next()) {
				int id = resultSet.getInt("DayID");
				String day = resultSet.getString("Name");
				days.add(new TDay(id, day));
			}
			result = true;

		} catch (SQLException e) {
			logger.error("SQL exception occured.",e);
			result = false;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return result;
	}


	/**
	 * @param day
	 * @return
	 */
	public static int queryDayID(String day) {
		String resultingDay;
		
		if ("M".equals(day)){	// Monday
			resultingDay = "Monday";
		} else if ("T".equals(day)){	// Tuesday
			resultingDay = "Tuesday";
		} else if ("W".equals(day)){
			resultingDay = "Wednesday";
		} else if ("Th".equals(day)){
			resultingDay = "Thursday";
		} else if ("F".equals(day)) {
			resultingDay = "Friday";
		} else if ("St".equals(day)) {
			resultingDay = "Saturday";
		} else if ("Sn".equals(day)){
			resultingDay = "Sunday";
		} else {
			resultingDay = "Unknown";
		}
		
		List<TDay> inDays = getDays();
		if (inDays == null || inDays.isEmpty()) {
			logger.error("Daylist is empty.");
			return 0;
		} else {
			return searchDayId(resultingDay);
		}
	}

	/**
	 * @param resultingDay
	 * @return
	 */
	private static int searchDayId(String resultingDay) {
		
		for(TDay day:getDays()) {
			if (day.getName().equals(resultingDay)) {
				return day.getId();
			}
		}
		return 0;
	}
	
	
	
}
