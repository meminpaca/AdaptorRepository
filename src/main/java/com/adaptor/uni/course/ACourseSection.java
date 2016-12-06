
package com.adaptor.uni.course;

import java.util.LinkedList;
import java.util.List;

import com.adaptor.uni.utils.TAdaptorConstants;

/**
 * Generic course class for Bogazici University.
 */
public abstract class ACourseSection implements ICourseSection {
	
	
	/** The course section name. */
	private String courseSectionName;
	
	/** The course section description. */
	private String courseSectionDescription;
	
	/** The instructor code. */
	private String instructorCode;
	
	/** The code sec. */
	private String codeSec;
	
	/** The credit. */
	private String credit;
	
	/** The ects. */
	private String ects;
	
	/** The instructor. */
	private String instructorName;
	
	/** The days. */
	private List<String> days;
	
	/** The hours. */
	private List<String> hours;
	
	/** The rooms. */
	private List<String> rooms;
	
	/** The exam date. */
	private String examDate;
	
	/** The final slot. */
	private String finalExamSlot;
	
	/** The req for dept. */
	private String reqForDept;
	
	/** The other code sec. */
	private String otherCodeSec;

	private String instructorSurname;
	
	private String type;

	private boolean hasLabOrPs;
	
	/**
	 */
	public ACourseSection() {
		super();
		this.codeSec = TAdaptorConstants.EMPTY_STRING;
		this.credit = TAdaptorConstants.EMPTY_STRING;
		this.ects = TAdaptorConstants.EMPTY_STRING;
		this.instructorName = TAdaptorConstants.EMPTY_STRING;
		this.instructorSurname = TAdaptorConstants.EMPTY_STRING;
		this.days = TAdaptorConstants.createEmptyList();
		this.hours = TAdaptorConstants.createEmptyList();
		this.rooms = TAdaptorConstants.createEmptyList();
		this.examDate = TAdaptorConstants.EMPTY_STRING;
		this.finalExamSlot = TAdaptorConstants.EMPTY_STRING;
		this.reqForDept = TAdaptorConstants.EMPTY_STRING;
		this.otherCodeSec = TAdaptorConstants.EMPTY_STRING;
		this.courseSectionName = TAdaptorConstants.EMPTY_STRING;
		this.courseSectionDescription = TAdaptorConstants.EMPTY_STRING;
		this.type = TAdaptorConstants.EMPTY_STRING;
		this.hasLabOrPs = false;
	}
	
	

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}



	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}



	/**
	 * Gets the code sec.
	 *
	 * @return the codeSec
	 */
	public String getCodeSec() {
		return codeSec;
	}

	/**
	 * Sets the code sec.
	 *
	 * @param codeSec the codeSec to set
	 */
	public void setCodeSec(String codeSec) {
		this.codeSec = codeSec;
	}
	
	
	/**
	 * @return the instructorCode
	 */
	public String getInstructorCode() {
		return instructorCode;
	}

	/**
	 * @param instructorCode the instructorCode to set
	 */
	public void setInstructorCode(String instructorCode) {
		this.instructorCode = instructorCode;
	}

	/**
	 * @return the courseSectionName
	 */
	public String getCourseSectionName() {
		return courseSectionName;
	}

	/**
	 * @param courseSectionName the courseSectionName to set
	 */
	public void setCourseSectionName(String courseSectionName) {
		this.courseSectionName = courseSectionName;
	}

	/**
	 * @return the courseSectionDescription
	 */
	public String getCourseSectionDescription() {
		return courseSectionDescription;
	}

	/**
	 * @param courseSectionDescription the courseSectionDescription to set
	 */
	public void setCourseSectionDescription(String courseSectionDescription) {
		this.courseSectionDescription = courseSectionDescription;
	}

	/**
	 * @return the instructorSurname
	 */
	public String getInstructorSurname() {
		return instructorSurname;
	}

	/**
	 * @param instructorSurname the instructorSurname to set
	 */
	public void setInstructorSurname(String instructorSurname) {
		this.instructorSurname = instructorSurname;
	}

	/**
	 * Gets the credit.
	 *
	 * @return the credit
	 */
	public String getCredit() {
		return credit;
	}

	/**
	 * Sets the credit.
	 *
	 * @param credit the credit to set
	 */
	public void setCredit(String credit) {
		this.credit = credit;
	}

	/**
	 * Gets the ects.
	 *
	 * @return the ects
	 */
	public String getEcts() {
		return ects;
	}

	/**
	 * Sets the ects.
	 *
	 * @param ects the ects to set
	 */
	public void setEcts(String ects) {
		this.ects = ects;
	}

	/**
	 * Gets the instructor.
	 *
	 * @return the instructor
	 */
	public String getInstructorName() {
		return instructorName;
	}

	/**
	 * Sets the instructor.
	 *
	 * @param instructor the instructor to set
	 */
	public void setInstructorName(String instructor) {
		this.instructorName = instructor;
	}

	/**
	 * Gets the days.
	 *
	 * @return the days
	 */
	public List<String> getDays() {
		return days;
	}

	/**
	 * Sets the days.
	 *
	 * @param days the days to set
	 */
	public void setDays(List<String> days) {
		this.days = days;
	}

	/**
	 * Gets the hours.
	 *
	 * @return the hours
	 */
	public List<String> getHours() {
		return hours;
	}

	/**
	 * Sets the hours.
	 *
	 * @param hours the hours to set
	 */
	public void setHours(List<String> hours) {
		this.hours = hours;
	}

	/**
	 * Gets the rooms.
	 *
	 * @return the rooms
	 */
	public List<String> getRooms() {
		return rooms;
	}

	/**
	 * Sets the rooms.
	 *
	 * @param rooms the rooms to set
	 */
	public void setRooms(List<String> rooms) {
		this.rooms = rooms;
	}

	/**
	 * Gets the exam date.
	 *
	 * @return the examDate
	 */
	public String getExamDate() {
		return examDate;
	}

	/**
	 * Sets the exam date.
	 *
	 * @param examDate the examDate to set
	 */
	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}

	/**
	 * Gets the final exam slot.
	 *
	 * @return the finalExamSlot
	 */
	public String getFinalExamSlot() {
		return finalExamSlot;
	}

	/**
	 * Sets the final exam slot.
	 *
	 * @param finalExamSlot the finalExamSlot to set
	 */
	public void setFinalExamSlot(String finalExamSlot) {
		this.finalExamSlot = finalExamSlot;
	}

	/**
	 * Gets the req for dept.
	 *
	 * @return the reqForDept
	 */
	public String getReqForDept() {
		return reqForDept;
	}

	/**
	 * Sets the req for dept.
	 *
	 * @param reqForDept the reqForDept to set
	 */
	public void setReqForDept(String reqForDept) {
		this.reqForDept = reqForDept;
	}

	/**
	 * Gets the other code sec.
	 *
	 * @return the otherCodeSec
	 */
	public String getOtherCodeSec() {
		return otherCodeSec;
	}

	/**
	 * Sets the other code sec.
	 *
	 * @param otherCodeSec the otherCodeSec to set
	 */
	public void setOtherCodeSec(String otherCodeSec) {
		this.otherCodeSec = otherCodeSec;
	}

	/**
	 * Sets the days by parsing days string.
	 *
	 * @param daysStr the new days
	 * TODO TBA olması durumunda boş ekle
	 */
	public void setDays(String daysStr) {
		if (TAdaptorConstants.TO_BE_ANNOUNCED_SHRTCT.equals(daysStr)){
			return;
		}
		String[] splits = daysStr.split("(?=\\p{Upper})");
		LinkedList<String> dayList = new LinkedList<String>();
		for (String d: splits) {
			dayList.add(d);
		}
		setDays(dayList);
		
	}

	/**
	 * Sets the hours.
	 *
	 * @param hoursStr the new hours
	 */
	public void setHours(String hoursStr) {
		List<String> hList = new LinkedList<String>();
		char[] hourChars = hoursStr.toCharArray();
		for (char c : hourChars) {
			hList.add(String.valueOf(c));
		}
		setHours(hList);
		
	}
	
	public void setHours(List<String> days, String hoursStr) {
		if (days == null || hoursStr == null || hoursStr.isEmpty()) {
			return;
		}
		// if #ofdays equals number of hour characters 
		if (days.size() == hoursStr.length()){
			setHours(hoursStr);
		} else if (hoursStr.length() > days.size()) {
			List<String> hoursArr = new LinkedList<String>();
			char[] hourChars = hoursStr.toCharArray();
			int [] nums = convertToInt(hourChars);
			int leapCharDiff = hoursStr.length() - days.size();
			for (int i = 0; i < nums.length; ++i) {
				if (leapCharDiff > 0 && nums[i] == 1) {
					// look at the next
					if ((i+1) < nums.length && nums[i + 1] < 5) {
						int newHour = nums[i] * 10 + nums[i+1];
						hoursArr.add( Integer.toString(newHour));
						--leapCharDiff;
						++i;
					} else {	// it consists of just one-character
						hoursArr.add(Integer.toString(nums[i]));
					}
				} else {
					hoursArr.add(Integer.toString(nums[i]));
				}
			}
			setHours(hoursArr);
		} else {	// days are more than hours, impossible
			System.err.println("ERRRRRROOOORRRRR!!!! Days are more than hours.");
			setHours(hoursStr);
		}
	}
	
	
	
	
	public int[] convertToInt(char[] chars) {
	    int[] nums = new int[chars.length];

	    for (int i = 0; i < chars.length; i++){
	        nums[i] = chars[i] - '0';
	    }
	    return nums;
	}



	/* (non-Javadoc)
	 * @see com.adaptor.uni.course.ICourseSection#setHasLabOrPs(boolean)
	 */
	public void setHasLabOrPs(boolean has) {
		this.hasLabOrPs = has;
		
	}



	/* (non-Javadoc)
	 * @see com.adaptor.uni.course.ICourseSection#hasLabOrPs()
	 */
	public boolean hasLabOrPs() {
		return hasLabOrPs;
	}
	
	
	
}
