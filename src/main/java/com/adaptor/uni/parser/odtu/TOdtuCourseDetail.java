/**
 * 
 */
package com.adaptor.uni.parser.odtu;

/**
 * @author emin.paca
 *
 */
public class TOdtuCourseDetail {
	public String courseName;
	public String ects;
	public String credit;
	
	
	/**
	 * @param courseName
	 * @param ects
	 * @param credit
	 */
	public TOdtuCourseDetail(String courseName, String ects, String credit) {
		super();
		this.courseName = courseName;
		this.ects = ects;
		this.credit = credit;
	}
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * @return the ects
	 */
	public String getEcts() {
		return ects;
	}
	/**
	 * @param ects the ects to set
	 */
	public void setEcts(String ects) {
		this.ects = ects;
	}
	/**
	 * @return the credit
	 */
	public String getCredit() {
		return credit;
	}
	/**
	 * @param credit the credit to set
	 */
	public void setCredit(String credit) {
		this.credit = credit;
	}
	
	
	

}
