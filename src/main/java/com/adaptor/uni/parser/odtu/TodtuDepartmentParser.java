/**
 * 
 */
package com.adaptor.uni.parser.odtu;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.adaptor.uni.course.ACourseSection;
import com.adaptor.uni.course.ICourse;
import com.adaptor.uni.course.TCourse;
import com.adaptor.uni.course.TCourseSection;
import com.adaptor.uni.model.IDepartment;
import com.adaptor.uni.model.TDepartment;
import com.adaptor.uni.parser.ADepartmentParser;
import com.adaptor.uni.parser.bogazici.TBogaziciDepartmentParser;
import com.adaptor.uni.utils.TOdtuParameters;
import com.adaptor.uni.utils.TUniversityCourseSectionTypes;

/**
 * @author emin.paca
 *
 */
public class TodtuDepartmentParser extends ADepartmentParser {
	
	/**
	 * Logger for logging  events
	 * */
	public static  final Logger logger = LogManager.getLogger(TodtuDepartmentParser.class);
	private String lastReceivedCookie = null;
	
	private static List<HourPair> odtuHourTable = new LinkedList<HourPair>();
	static {
		odtuHourTable.add(new HourPair(19,"08:40","09:30"));
		odtuHourTable.add(new HourPair(20,"09:40","10:30"));
		odtuHourTable.add(new HourPair(21,"10:40","11:30"));
		odtuHourTable.add(new HourPair(22,"11:40","12:30"));
		odtuHourTable.add(new HourPair(23,"12:40","13:30"));
		odtuHourTable.add(new HourPair(24,"13:40","14:30"));
		odtuHourTable.add(new HourPair(25,"14:40","15:30"));
		odtuHourTable.add(new HourPair(26,"15:40","16:30"));
		odtuHourTable.add(new HourPair(27,"16:40","17:30"));
		odtuHourTable.add(new HourPair(28,"17:40","18:30"));
		odtuHourTable.add(new HourPair(29,"18:40","19:30"));
		odtuHourTable.add(new HourPair(30,"19:40","20:30"));
		odtuHourTable.add(new HourPair(31,"20:40","21:30"));
		odtuHourTable.add(new HourPair(32,"21:40","22:30"));
		
	}

	/* (non-Javadoc)
	 * @see com.adaptor.uni.parser.IDepartmentParser#parse(java.lang.String, com.adaptor.uni.model.TDepartment)
	 */
	public IDepartment parse(String departmentUrl, TDepartment department) {
		Document doc = doPostRequest(department);
		
		if (doc == null) {
			return null;
		}
		parseDocument(department,doc);
		return department;
		
	}

	/**
	 * @param department
	 * @param doc
	 */
	private void parseDocument(TDepartment department, Document doc) {
		Elements departmentElements = doc.select("table[border=1]");
		boolean isFirstRowSkipped = false;
		
		Map<String,TOdtuCourseDetail> courseDetailsMap =  getCourseDetailsMap(department);
		
		if (courseDetailsMap == null) {
			return;
		}
		
		if (departmentElements.size() > 0) {
			List<ACourseSection> trCourses = new LinkedList<ACourseSection>();
			Elements departmentsRows = departmentElements.select("tr");
			Iterator<Element> depIt = departmentsRows.iterator();
			while (depIt.hasNext()){
				Element depElementTr = depIt.next();
				if (!isFirstRowSkipped) {
					isFirstRowSkipped = true;
					continue;
				}
				Elements depElementTds = depElementTr.select("td");
				
				/**
				 * Course Code	Section	Instructor 1	Day 1	Start Hour-1	End Hour-1	Classroom 1,
				 * Day 2	Start Hour-2	End Hour-2	Classroom 2	,
				 * Day 3	Start Hour-3	End Hour-3	Classroom 3	,
				 * Day 4	Start Hour-4	End Hour-4	Classroom 4,
				 * Day 5	Start Hour-5	End Hour-5	Classroom 5,
				 * Capacity	Instructor 2
				 * */
				String courseCode = depElementTds.get(0).text();
				String section = depElementTds.get(1).text();
				String instructor1 = depElementTds.get(2).text();
				
				// day1
				String day1 = depElementTds.get(3).text();
				String startHour1 = depElementTds.get(4).text();
				String endHour1 = depElementTds.get(5).text();
				String classRoom1 = depElementTds.get(6).text();
				
				// day2
				String day2 = depElementTds.get(7).text();
				String startHour2 = depElementTds.get(8).text();
				String endHour2 = depElementTds.get(9).text();
				String classRoom2 = depElementTds.get(10).text();
				
				// day3
				String day3 = depElementTds.size() > 11 ? depElementTds.get(11).text() : "";
				String startHour3 = depElementTds.size() > 12 ? depElementTds.get(12).text() : "";
				String endHour3 = depElementTds.size() > 13 ? depElementTds.get(13).text() : "";
				String classRoom3 = depElementTds.size() > 14 ? depElementTds.get(14).text() : "";
				
				// day4
				String day4 = depElementTds.size() > 15 ? depElementTds.get(15).text() : "";
				String startHour4 = depElementTds.size() > 16 ? depElementTds.get(16).text() : "";;
				String endHour4 = depElementTds.size() > 17 ? depElementTds.get(17).text() : "";
				String classRoom4 = depElementTds.size() > 18 ? depElementTds.get(18).text() : "";
				
				// day5
				String day5 = depElementTds.size() > 19 ? depElementTds.get(19).text() : "";
				String startHour5 = depElementTds.size() > 20 ? depElementTds.get(20).text() : "";
				String endHour5 = depElementTds.size() > 21 ? depElementTds.get(21).text() : "";
				String classRoom5 = depElementTds.size() > 22 ? depElementTds.get(22).text() : "";

				String capacity = depElementTds.size() > 23 ? depElementTds.get(23).text() : "";
				
				String trimmedInstructor1 = instructor1.trim();
				int firstIndexOfSpace = trimmedInstructor1.indexOf(" ");
				String instructorCode = trimmedInstructor1.substring(0,firstIndexOfSpace );
				trimmedInstructor1 = trimmedInstructor1.substring(firstIndexOfSpace + 1);
				firstIndexOfSpace = trimmedInstructor1.indexOf(" ");

				String instructorSurname = "";
				String instructorName = "";
				// staff
				if (firstIndexOfSpace == -1 && "1".equals(instructorCode)){
					instructorName = trimmedInstructor1;
				} else {
					instructorName = trimmedInstructor1.substring(firstIndexOfSpace + 1);
					instructorSurname = trimmedInstructor1.substring(0,firstIndexOfSpace );
					
				}
				
				
				
				List<String> allHours = new LinkedList<String>();
				List<String> allRooms = new LinkedList<String>();
				List<String> allDays = new LinkedList<String>();
				StringBuilder roomBuilder = new StringBuilder();
				
				List<String> hours1 = generateHoursSlots(startHour1,endHour1);
				allDays.addAll(produceNumOfStringList(convertToDayCode(day1),hours1.size()));
				allRooms.addAll(produceNumOfStringList(classRoom1,hours1.size()));
				
				List<String> hours2 = generateHoursSlots(startHour2,endHour2);
				allDays.addAll(produceNumOfStringList(convertToDayCode(day2),hours2.size()));
				allRooms.addAll(produceNumOfStringList(classRoom2,hours2.size()));

				List<String> hours3 = generateHoursSlots(startHour3,endHour3);
				allDays.addAll(produceNumOfStringList(convertToDayCode(day3),hours3.size()));
				allRooms.addAll(produceNumOfStringList(classRoom3,hours3.size()));

				List<String> hours4 = generateHoursSlots(startHour4,endHour4);
				allDays.addAll(produceNumOfStringList(convertToDayCode(day4),hours4.size()));
				allRooms.addAll(produceNumOfStringList(classRoom4,hours4.size()));

				List<String> hours5 = generateHoursSlots(startHour5,endHour5);
				allDays.addAll(produceNumOfStringList(convertToDayCode(day5),hours4.size()));
				allRooms.addAll(produceNumOfStringList(classRoom5,hours5.size()));
				
				allHours.addAll(hours1);
				allHours.addAll(hours2);
				allHours.addAll(hours3);
				allHours.addAll(hours4);
				allHours.addAll(hours5);
				
				
				String courseSectionStr = courseCode +"."+ section;
				TOdtuCourseDetail courseDetail = courseDetailsMap.get(courseCode);
				
				if (courseDetail == null) {
					logger.warn("Course detail is null for " + courseCode + " and skipped.");
					continue;
				}
				
				TCourseSection courseSection = new TCourseSection();
				courseSection.setCodeSec(courseSectionStr);
				courseSection.setCourseSectionDescription(courseDetail.getCourseName());
				courseSection.setCourseSectionName(courseDetail.getCourseName());
				courseSection.setCredit(courseDetail.getCredit());
				courseSection.setEcts(courseDetail.getEcts());
				courseSection.setInstructorName(instructorName);
				courseSection.setInstructorSurname(instructorSurname);
				courseSection.setInstructorCode(instructorCode);
				courseSection.setDays(allDays);
				courseSection.setHours(allHours);
				courseSection.setRooms(allRooms);
				courseSection.setType(TUniversityCourseSectionTypes.COURSE);

						
				trCourses.add(courseSection);
				ICourse courseObj = department.getCourseMap().get(courseCode);
				if (courseObj == null) {
					courseObj = new TCourse(courseDetail.getCourseName(),courseCode , courseDetail.getCourseName());
					department.getCourseMap().put(courseCode, courseObj);
				}
				courseObj.getCourseSections().add(courseSection);
			}
			
		} else {
			logger.error("There is no department to be parsed.");
		}		
	}

	/**
	 * @param classRoom1
	 * @param size
	 * @return
	 */
	private List<String> produceNumOfStringList(String classRoom, int size) {
		List<String> generated = new LinkedList<String>();
		for (int i = 0; i < size; ++i) {
			generated.add(classRoom);
		}
		return generated;
	}

	/**
	 * @param size
	 * @return
	 */
	private String produceNumOfString(String dayCode,int size) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size; ++i) {
			builder.append(dayCode);
		}
		return builder.toString();
	}

	/**
	 * @param startHour1
	 * @param endHour1
	 * @return
	 */
	private List<String> generateHoursSlots(String startHour1, String endHour1) {
		
		List<String > slots = new LinkedList<String>();
		if ("".equals(startHour1) ||"".equals(endHour1)) {
			return slots;
		}
		if (!startHour1.endsWith("0")){
			startHour1 = startHour1 + "0";
		}
		if (!endHour1.endsWith("0")){
			endHour1 = endHour1 + "0";
		}
		
		Iterator<HourPair> it = odtuHourTable.iterator();
		int startingSlot = 0;
		int endingSlot = 0;
		while (it.hasNext()) {
			HourPair next = it.next();
			if (startHour1.equals(next.getStart())){
				startingSlot = next.getTimeTableId();
			}
			if (endHour1.equals(next.getEnd())){
				endingSlot = next.getTimeTableId();
			}
		}
		
		for (int i = startingSlot; i<= endingSlot; ++i) {
			slots.add(String.valueOf(i));
		}
		return slots;
	}

	/**
	 * @param day
	 * @return
	 */
	private String convertToDayCode(String day) {
		if ("".equals(day)){
			return "";
		}
		if ("Monday".equals(day)){
			return "M";
		}
		if ("Tuesday".equals(day)){
			return "T";
		}
		if ("Wednesday".equals(day)){
			return "W";
		}
		if ("Thursday".equals(day)){
			return "Th";
		}
		
		if ("Friday".equals(day)){
			return "F";
		}
		
		return "";
	}

	/**
	 * @param department
	 * @return
	 */
	private Map<String,TOdtuCourseDetail> getCourseDetailsMap(TDepartment department) {
		Document doc = null;
		try {
			Connection conn = Jsoup.connect(TOdtuParameters.COURSE_DETAIL_URL)
					.data(TOdtuParameters.TEXT_WITHOUT_THESIS, "1")
					.data(TOdtuParameters.SELECT_DEPT, parseNumericDepartmentCode(department.getDepartmentCode()))
					.data(TOdtuParameters.SELECT_SEMESTER, TOdtuParameters.POST_DATA_SEMESTER_VALUE)
					.data(TOdtuParameters.SUBMIT_COURSE_LIST, "Submit")
					.data(TOdtuParameters.HIDDEN_REDIR, "Login")
					.method(Connection.Method.POST);
			
			Response response = conn.execute();
			doc = Jsoup.parse(new String(response.bodyAsBytes(),"ISO-8859-9"));
			return fillCoursesToCourseList(doc);
		} catch(SocketTimeoutException soe){
			logger.error("Timeout occured while getting department response cookie: " + department.getDepartmentCode(),soe);
			return null;
		} 
		catch (IOException e) {
			logger.error("Connection problem occured while getting department response cookie:: " + department.getDepartmentCode(),e);
			return null;
		}
	}
	
	

	/**
	 * @param doc
	 * @return
	 */
	private Map<String,TOdtuCourseDetail> fillCoursesToCourseList(Document doc) {
		if (doc == null) {
			return null;
		}
		
		Elements actualTable = doc.select("table[border=1]");
		Map<String,TOdtuCourseDetail> courseDetailMap = null;
		if (actualTable == null) {
			return null;
		}		
		boolean isFirstRowSkipped = false;
		Elements coursesRows = actualTable.select("tr");
		Iterator<Element> depIt = coursesRows.iterator();
		while (depIt.hasNext()){
			Element courseTr = depIt.next();
			if (!isFirstRowSkipped) {
				isFirstRowSkipped = true;
				continue;
			}
			Elements courseTds = courseTr.select("td");
			
			/**
			 * Course Code	Section	Instructor 1	Day 1	Start Hour-1	End Hour-1	Classroom 1,
			 * Day 2	Start Hour-2	End Hour-2	Classroom 2	,
			 * Day 3	Start Hour-3	End Hour-3	Classroom 3	,
			 * Day 4	Start Hour-4	End Hour-4	Classroom 4,
			 * Day 5	Start Hour-5	End Hour-5	Classroom 5,
			 * Capacity	Instructor 2
			 * */
			String courseCode = courseTds.get(1).text();
			String courseName = courseTds.get(2).text();
			String ects = courseTds.get(3).text();
			String credit = courseTds.get(4).text();
			credit = credit.substring(0,credit.indexOf("(")).trim();
			
 			if (courseDetailMap == null) {
				courseDetailMap = new HashMap<String, TOdtuCourseDetail>();
			}
			courseDetailMap.put(courseCode, new TOdtuCourseDetail(courseName, ects, credit));
			
		}
		return courseDetailMap;
	}

	/**
	 * @param department
	 * @return
	 */
	private Document doPostRequest(TDepartment department) {
		Document doc = null;
		try {
			Connection conn = Jsoup.connect(TOdtuParameters.SCHEDULE_LIST_URL)
					.data(TOdtuParameters.INPUT_DEPT_CODE, parseNumericDepartmentCode(department.getDepartmentCode()))
					  .data(TOdtuParameters.INPUT_SEMESTER, TOdtuParameters.POST_DATA_SEMESTER_VALUE)
					  .data(TOdtuParameters.SUBMIT_NAME, TOdtuParameters.SUBMIT_NAME_COURSE_SCHEDULE_VALUE)
					  .data(TOdtuParameters.SA_FORM_NAME, TOdtuParameters.SA_FORM_NAME_COURSE_SCHEDULE_VALUE)
					  .method(Connection.Method.POST);
			
			Response response = conn.execute();
			doc = Jsoup.parse(new String(response.bodyAsBytes(),"ISO-8859-9"));
			
		} catch(SocketTimeoutException soe){
			logger.error("Timeout occured while getting: " + department.getDepartmentName(),soe);
			return null;
		} 
		catch (IOException e) {
			logger.error("Connection problem occured while getting: " + department.getDepartmentName(),e);
			return null;
		}
		System.out.println(doc);
		return doc;
	}

	/**
	 * @param departmentCode
	 * @return
	 */
	private String parseNumericDepartmentCode(String departmentCode) {
		if (departmentCode == null || "".equals(departmentCode)){
			return departmentCode;
		}
		if (departmentCode.contains("-")){
			return departmentCode.substring(0,departmentCode.indexOf("-"));
		}else{
			return departmentCode;
		}
	}

}
