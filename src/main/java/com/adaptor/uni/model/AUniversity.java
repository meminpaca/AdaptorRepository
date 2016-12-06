/**
 * 
 */
package com.adaptor.uni.model;

import java.util.LinkedList;
import java.util.List;

/**
 * The Class AUniversity.
 *
 * @author emin.paca
 */
public abstract class AUniversity implements IUniversity {
	
	/** The university name. */
	private String universityName;
	
	/** The university code. */
	private String universityCode;
	
	/** The semester. */
	private String semester;
	
	private int universityID;
	
	private List<IDepartment> departments;

	private int semesterID;
	
	public AUniversity(String uniName,String uniCode,int uniID,String uniSemester,int semesterID) {
		this.universityName = uniName;
		this.universityCode = uniCode;
		this.semester = uniSemester;
		this.universityID = uniID;
		this.departments = new LinkedList<IDepartment>();
		this.semesterID = semesterID;
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.model.IUniversity#getUniversityName()
	 */
	public String getUniversityName() {
		return universityName;
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.model.IUniversity#getSemester()
	 */
	public String getSemester() {
		// TODO Auto-generated method stub
		return semester;
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.model.IUniversity#getUniversityCode()
	 */
	public String getUniversityCode() {
		// TODO Auto-generated method stub
		return universityCode;
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.model.IUniversity#getDepartments()
	 */
	public List<IDepartment> getDepartments() {
		// TODO Auto-generated method stub
		return departments;
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.model.IUniversity#addDepartment(com.adaptor.uni.model.IDepartment)
	 */
	public boolean addDepartment(IDepartment department) {
		return departments.add(department);
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.model.IUniversity#getUniversityID()
	 */
	public int getUniversityID() {
		// TODO Auto-generated method stub
		return universityID;
	}
	
	public int getSemesterID() {
		return semesterID;
	}
	
	
	
	
	
	
	

}
