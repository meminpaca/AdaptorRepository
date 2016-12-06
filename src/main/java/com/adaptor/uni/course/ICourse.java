/**
 * 
 */
package com.adaptor.uni.course;

import java.util.List;

/**
 * @author emin.paca
 *
 */
public interface ICourse {
	
	/**
	 * Gets the course code.
	 *
	 * @return the course code
	 */
	public String getCourseCode();
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription();
	
	/**
	 *  The course sections.
	 *
	 * @return the course sections
	 */
	public List<ICourseSection> getCourseSections();

}
