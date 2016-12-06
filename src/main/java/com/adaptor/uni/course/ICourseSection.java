package com.adaptor.uni.course;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * Interface for courses.
 */
public interface ICourseSection {

	/**
	 * Gets the code sec.
	 *
	 * @return the code sec
	 */
	public String getCodeSec() ;
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType();
	
	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type);

	/**
	 * Sets the code sec.
	 *
	 * @param codeSec the codeSec to set
	 */
	public void setCodeSec(String codeSec);

	/**
	 * Gets the course section name.
	 *
	 * @return the course section name
	 */
	public String getCourseSectionName();

	/**
	 * Gets the course section description.
	 *
	 * @return the course section description
	 */
	public String getCourseSectionDescription();
	
	/**
	 * Gets the credit.
	 *
	 * @return the credit
	 */
	public String getCredit();

	/**
	 * Sets the credit.
	 *
	 * @param credit the credit to set
	 */
	public void setCredit(String credit);

	/**
	 * Gets the ects.
	 *
	 * @return the ects
	 */
	public String getEcts();

	/**
	 * Sets the ects.
	 *
	 * @param ects the ects to set
	 */
	public void setEcts(String ects);

	/**
	 * Gets the instructor.
	 *
	 * @return the instructor
	 */
	public String getInstructorName();
	
	/**
	 * Gets the ınstructor surname.
	 *
	 * @return the ınstructor surname
	 */
	public String getInstructorSurname();

	/**
	 * Sets the instructor.
	 *
	 * @param instructor the instructor to set
	 */
	public void setInstructorName(String instructor);

	/**
	 * Gets the days.
	 *
	 * @return the days
	 */
	public List<String> getDays();

	/**
	 * Sets the days.
	 *
	 * @param days the days to set
	 */
	public void setDays(List<String> days);

	/**
	 * Gets the hours.
	 *
	 * @return the hours
	 */
	public List<String> getHours();

	/**
	 * Sets the hours.
	 *
	 * @param hours the hours to set
	 */
	public void setHours(List<String> hours);

	/**
	 * Gets the rooms.
	 *
	 * @return the rooms
	 */
	public List<String> getRooms();

	/**
	 * Sets the rooms.
	 *
	 * @param rooms the rooms to set
	 */
	public void setRooms(List<String> rooms);

	/**
	 * Gets the exam date.
	 *
	 * @return the examDate
	 */
	public String getExamDate();

	/**
	 * Sets the exam date.
	 *
	 * @param examDate the examDate to set
	 */
	public void setExamDate(String examDate);

	/**
	 * Gets the final exam slot.
	 *
	 * @return the finalExamSlot
	 */
	public String getFinalExamSlot();

	/**
	 * Sets the final exam slot.
	 *
	 * @param finalExamSlot the finalExamSlot to set
	 */
	public void setFinalExamSlot(String finalExamSlot);
	/**
	 * Gets the req for dept.
	 *
	 * @return the reqForDept
	 */
	public String getReqForDept();

	/**
	 * Sets the req for dept.
	 *
	 * @param reqForDept the reqForDept to set
	 */
	public void setReqForDept(String reqForDept);

	/**
	 * Gets the other code sec.
	 *
	 * @return the otherCodeSec
	 */
	public String getOtherCodeSec();
	/**
	 * Sets the other code sec.
	 *
	 * @param otherCodeSec the otherCodeSec to set
	 */
	public void setOtherCodeSec(String otherCodeSec);
	/**
	 * Sets the days by parsing days string.
	 *
	 * @param daysStr the new days
	 * TODO TBA olması durumunda boş ekle
	 */
	public void setDays(String daysStr);

	/**
	 * Sets the hours.
	 *
	 * @param hoursStr the new hours
	 */
	public void setHours(String hoursStr);

	/**
	 * Gets the ınstructor code.
	 *
	 * @return the ınstructor code
	 */
	public String getInstructorCode();
	
	/**
	 * Sets the checks for lab or ps.
	 *
	 * @param has the new checks for lab or ps
	 */
	public void setHasLabOrPs(boolean has);
	
	/**
	 * Checks for lab or ps.
	 *
	 * @return true, if successful
	 */
	public boolean hasLabOrPs();
	
}
