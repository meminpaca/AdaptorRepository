/**
 * 
 */
package com.adaptor.uni.course;

import java.util.LinkedList;
import java.util.List;

/**
 * The Class ACourse.
 *
 * @author emin.paca
 */
public abstract class ACourse implements ICourse {
	
	/** The name. */
	private String name;
	
	/** The course code. */
	private String courseCode;
	
	/** The description. */
	private String description;
	
	/** The course sections. */
	private List<ICourseSection> courseSections;
	
	
	/**
	 * Instantiates a new a course.
	 *
	 * @param name the name
	 * @param courseCode the course code
	 * @param description the description
	 */
	public ACourse(String name,String courseCode,String description){
		this.name = name;
		this.courseCode = courseCode;
		this.description = description;
		this.courseSections = new LinkedList<ICourseSection>();
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.course.ICourse#getCourseCode()
	 */
	public String getCourseCode() {
		// TODO Auto-generated method stub
		return courseCode;
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.course.ICourse#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.course.ICourse#getDescription()
	 */
	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.course.ICourse#getCourseSections()
	 */
	public List<ICourseSection> getCourseSections() {
		return courseSections;
	}
	
	

}
