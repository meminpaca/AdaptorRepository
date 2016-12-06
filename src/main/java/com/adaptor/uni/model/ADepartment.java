/**
 * 
 */
package com.adaptor.uni.model;

import java.util.HashMap;
import java.util.Map;

import com.adaptor.uni.course.ICourse;

// TODO: Auto-generated Javadoc
/**
 * The Class ADepartment.
 *
 * @author emin.paca
 */
public abstract class ADepartment implements IDepartment{

	/** The department name. */
	private String departmentName;
	
	/** The deparment code. */
	private String deparmentCode;
	
	/** The faculty id. */
	private int facultyId;
	
	/** The course map. */
	private Map<String,ICourse> courseMap;
	
	
	
	/**
	 * Instantiates a new a department.
	 *
	 * @param depName the dep name
	 * @param depCode the dep code
	 * @param fID the f ID
	 */
	public ADepartment(String depName,String depCode,int fID) {
		this.departmentName = depName;
		this.deparmentCode = depCode;
		this.facultyId = fID;
		this.courseMap = new HashMap<String, ICourse>();
	}
	
	/* (non-Javadoc)
	 * @see com.adaptor.uni.model.IDepartment#getDepartmentName()
	 */
	public String getDepartmentName() {
		// TODO Auto-generated method stub
		return departmentName;
	}
	/* (non-Javadoc)
	 * @see com.adaptor.uni.model.IDepartment#getDepartmentCode()
	 */
	public String getDepartmentCode() {
		// TODO Auto-generated method stub
		return deparmentCode;
	}
	
	/* (non-Javadoc)
	 * @see com.adaptor.uni.model.IDepartment#getFacultyID()
	 */
	public int getFacultyID() {
		// TODO Auto-generated method stub
		return facultyId;
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.model.IDepartment#getCourseMap()
	 */
	public Map<String, ICourse> getCourseMap() {
		return courseMap;
	}
	
	
	
	
}
