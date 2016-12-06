/**
 * 
 */
package com.adaptor.uni.model;

import java.util.Map;

import com.adaptor.uni.course.ICourse;

/**
 * The Interface IDepartment.
 *
 * @author emin.paca
 */
public interface IDepartment {
	
	/**
	 * Gets the department name.
	 *
	 * @return the department name
	 */
	public String getDepartmentName();
	
	/**
	 * Gets the department code.
	 *
	 * @return the department code
	 */
	public String getDepartmentCode();
	
	/**
	 * Gets the faculty ID.
	 *
	 * @return the faculty ID
	 */
	public int getFacultyID();
	
	/** The course map. */
	public Map<String, ICourse> getCourseMap();
	

}
