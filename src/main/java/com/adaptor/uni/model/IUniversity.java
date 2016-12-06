/**
 * 
 */
package com.adaptor.uni.model;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface IUniversity.
 *
 * @author emin.paca
 */
public interface IUniversity {
	
	
	/**
	 * Gets the university name.
	 *
	 * @return the university name
	 */
	public String getUniversityName();
	
	/**
	 * Gets the semester.
	 *
	 * @return the semester
	 */
	public String getSemester();
	
	/**
	 * Gets the university code.
	 *
	 * @return the university code
	 */
	public String getUniversityCode();
	
	/**
	 * Gets the university ID.
	 *
	 * @return the university ID
	 */
	public int getUniversityID();
	

	/**
	 * Gets the departments.
	 *
	 * @return the departments
	 */
	public List<IDepartment> getDepartments();
	
	/**
	 * Adds the department.
	 *
	 * @param department the department to be added
	 * @return true, if successful
	 */
	public boolean addDepartment(IDepartment department);
	
	/**
	 * Gets the semester ID.
	 *
	 * @return the semester ID
	 */
	public int getSemesterID();

}
