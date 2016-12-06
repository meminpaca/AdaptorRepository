package com.adaptor.uni.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Stores all constants used in application use it for avoiding magic numbers
 * */
public class TAdaptorConstants {
	
	/**
	 * Specifies parse period in minute
	 * */
	public static final int PARSE_PERIOD = 24 * 60 * 60 * 1000;	// Daily period
	
	// JDBC driver name and database URL
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	public static final String DB_URL = "jdbc:mysql://bumarmaradb.ckpgjyxtfncq.eu-west-1.rds.amazonaws.com:3306/BUMARMARA?useUnicode=yes&characterEncoding=UTF-8";
	
	//  Database credentials
	public static final String DB_USER = "bumarmara";
	public static final String DB_PASS = "bumarmara2016";
	   
	
	public static final String DEFAULT_SEMESTER_VALUE = "semester";
	public static final String DEFAULT_DEPARTMENT_VALUE = "department";
	public static final String TO_BE_ANNOUNCED_SHRTCT = "TBA";

	public static final String EMPTY_STRING = "";

	/**
	 * Creates the empty list.
	 *
	 * @return the list
	 */
	public static <T> List<T> createEmptyList() {
		return new LinkedList<T>();
	}

}
