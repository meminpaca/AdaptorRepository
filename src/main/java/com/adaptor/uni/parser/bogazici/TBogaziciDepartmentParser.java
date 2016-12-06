package com.adaptor.uni.parser.bogazici;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.adaptor.uni.App;
import com.adaptor.uni.course.ICourse;
import com.adaptor.uni.course.ICourseSection;
import com.adaptor.uni.course.ACourseSection;
import com.adaptor.uni.course.TCourse;
import com.adaptor.uni.course.TCourseSection;
import com.adaptor.uni.model.IDepartment;
import com.adaptor.uni.model.TDepartment;
import com.adaptor.uni.parser.ADepartmentParser;
import com.adaptor.uni.parser.utils.TParserHelper;
import com.adaptor.uni.utils.TAdaptorConstants;
import com.adaptor.uni.utils.TUniversityCourseSectionTypes;

/**
 * The Class TBogaziciDepartmentParser.
 */
public class TBogaziciDepartmentParser extends ADepartmentParser {
	
	/**
	 * Logger for logging  events
	 * */
	public static  final Logger logger = LogManager.getLogger(TBogaziciDepartmentParser.class);
	
	public IDepartment parse(String url,TDepartment bogaziciDepartment) {
		//String url = "http://registration.boun.edu.tr/scripts/sch.asp?donem=2015/2016-2&kisaadi=CMPE&bolum=COMPUTER+ENGINEERING";
		TDepartment dep = parseDepartmentSite(url,bogaziciDepartment);
		return dep;
	}

	/**
	 * Parses course site which addressed by url
	 * @param departmentUrl Url of the course web site
	 * */
	private TDepartment parseDepartmentSite(String departmentUrl,TDepartment bogaziciDepartment) {
		try {
			Document document = Jsoup.connect(departmentUrl).get();
			Elements answerers = document.getElementsByTag("table");			
			if (answerers.size() < 4) {
				logger.error("Missing tables on department site : " + departmentUrl);
				return null;
			}
			parseCourseSemesterAndDepartment(bogaziciDepartment,answerers.get(0));
			parseCourseSlots(bogaziciDepartment,answerers.get(1));
			parseFinalExamSlots(bogaziciDepartment,answerers.get(2));
			parseCoursesAndDetails(bogaziciDepartment,answerers.get(3));
			return bogaziciDepartment;

		} catch(SocketTimeoutException soe){
			logger.error("Connection problem occured while getting: " + departmentUrl);
			return null;
		} 
		catch (IOException e) {
			logger.error("Connection problem occured while getting: " + departmentUrl,e);
			return null;
		}
	}
	private void parseCoursesAndDetails(TDepartment bDepartment, Element element) {
		Elements trElements = element.getElementsByTag("tr");
		if (trElements.size() > 1) {
			
			// Code.Sec
			String codeSec = TAdaptorConstants.EMPTY_STRING;
			
			// Desc.
			String desc = TAdaptorConstants.EMPTY_STRING;
			
			// Cr.[Credit]
			String credit = TAdaptorConstants.EMPTY_STRING;
			
			// Ects[European credit transfer and accumulation system]
			String ects = TAdaptorConstants.EMPTY_STRING;
			
			List<ACourseSection> trCourses = new LinkedList<ACourseSection>(); 
			for (int index = 1; index < trElements.size(); ++index) {
				Element courseElement = trElements.get(index);
				List<Element> tdCourseElements = courseElement.getElementsByTag("td");
				
				
				codeSec = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(0).getElementsByTag("font").text(),codeSec);
				codeSec = codeSec.replaceAll(" ", "");
				String courseCode = parseCourseCode(codeSec);
				
//				getCourseOrCreateOne(bDepartment.)
				
				String sectionCode = parseCourseSection(codeSec);
				
				
				desc = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(1).text(),desc);
				
				// Name
				String courseSectionName = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(2).text(),TAdaptorConstants.EMPTY_STRING);
				
				credit = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(3).text(),TAdaptorConstants.EMPTY_STRING);

				ects = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(4).text(),TAdaptorConstants.EMPTY_STRING);
				
				String quota = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(5).text(),TAdaptorConstants.EMPTY_STRING);
				// Instr.
				String instr = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(6).text(),TAdaptorConstants.EMPTY_STRING);
				
				// Days
				String days = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(7).text(),TAdaptorConstants.EMPTY_STRING);

				// Hours
				String hours = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(8).text(),TAdaptorConstants.EMPTY_STRING);

				// Rooms
				List<String> rooms = new LinkedList<String>();
				Elements roomElements = tdCourseElements.get(9).getElementsByTag("span");
				for(Element roomElement: roomElements) {
					String text = roomElement.text();
					if ( text == null || "".equals(text) || text.contains("|") ) {
						continue;
					} else {
						rooms.add(TParserHelper.getDefaultOrParsedPart(text,TAdaptorConstants.EMPTY_STRING));
					}
				}
				
				// Exam
				String exam = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(10).text(),TAdaptorConstants.EMPTY_STRING);
				
				// Sl.
				String sl = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(11).text(),TAdaptorConstants.EMPTY_STRING);
				
				// Required for Dept.
				String reqForDept = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(12).text(),TAdaptorConstants.EMPTY_STRING);
				
				
//				String otherCodeSec = TParserHelper.getDefaultOrParsedPart(tdCourseElements.get(13).text(),TAdaptorConstants.EMPTY_STRING);
				
				TCourseSection courseSection = new TCourseSection();
				courseSection.setCodeSec(codeSec);
				courseSection.setCourseSectionDescription(desc);
				courseSection.setCourseSectionName(courseSectionName);
				courseSection.setCredit(credit);
				courseSection.setEcts(ects);
				courseSection.setInstructorName(instr);
				courseSection.setInstructorCode(instr);
				courseSection.setDays(days);
				courseSection.setHours(courseSection.getDays(),hours);
				courseSection.setRooms(rooms);
				courseSection.setExamDate(exam);
				courseSection.setFinalExamSlot(sl);
				courseSection.setReqForDept(reqForDept);
//				courseSection.setOtherCodeSec(otherCodeSec);
				courseSection.setType(searchType(courseSectionName));
						
				trCourses.add(courseSection);
				checkAndSetHasLabOrPsFlag(trCourses,courseSection);
				
				
				ICourse courseObj = bDepartment.getCourseMap().get(courseCode);
				
				
				if (courseObj == null) {
					courseObj = new TCourse(courseSectionName,courseCode , courseSectionName);
					bDepartment.getCourseMap().put(courseCode, courseObj);
				}
				courseObj.getCourseSections().add(courseSection);
				
				System.out.println(codeSec + " | " + desc + " | " + courseSectionName + " | " 
				+ credit + " | " + ects + " | " + instr + " | " + days + " | " + hours + " | " 
				+ rooms + " | " + exam + " | " + sl + " | " + reqForDept + " | " );
				
			}
			//bDepartment.setCourses(trCourses);
		}	
		
	}

	/**
	 * @param trCourses
	 * @param courseSection
	 */
	private void checkAndSetHasLabOrPsFlag(List<ACourseSection> trCourses, TCourseSection courseSection) {
		if (TUniversityCourseSectionTypes.LAB.equals(courseSection.getType())||TUniversityCourseSectionTypes.PS.equals(courseSection.getType())) {
			courseSection.setHasLabOrPs(true);
			for(int i = trCourses.size() - 1; i >=0; --i){
				ICourseSection next = trCourses.get(i);
				if (next.getCodeSec()==courseSection.getCodeSec() && TUniversityCourseSectionTypes.COURSE.equals(next.getType()))
				{
					next.setHasLabOrPs(true);
				}
			}
		}
	}

	/**
	 * @param courseSectionName
	 * @return
	 */
	private String searchType(String courseSectionName) {
		if (courseSectionName == null || courseSectionName.equals(TAdaptorConstants.EMPTY_STRING)) {
			return TUniversityCourseSectionTypes.COURSE;
		}
		if (courseSectionName.startsWith(TUniversityCourseSectionTypes.LAB)) {
			return TUniversityCourseSectionTypes.LAB;
		}
		if (courseSectionName.startsWith(TUniversityCourseSectionTypes.PS)) {
			return TUniversityCourseSectionTypes.PS;
		}
		return TUniversityCourseSectionTypes.COURSE;
	}

	/**
	 * Parses the course code.
	 *
	 * @param codeSec the code sec
	 * @return the string
	 */
	private String parseCourseCode(String codeSec) {
		if (codeSec == null ) {
			return "";
		}
		return codeSec.split("\\.")[0];
	}
	
	/**
	 * Parses the course section.
	 *
	 * @param codeSec the code sec
	 * @return the string
	 */
	private String parseCourseSection(String codeSec) {
		if (codeSec == null ) {
			return "";
		}
		return codeSec.split("\\.")[1];
	}

	private void parseFinalExamSlots(TDepartment bDepartment, Element element) {
		Elements tdElements = element.getElementsByTag("td");
		List<String> finalExamSlots = new LinkedList<String>();
		if (tdElements.size() > 1) {
			for (int index = 1; index < tdElements.size(); ++index) {
				Element slotElement = tdElements.get(index);
				List<Node> childNodes = slotElement.childNodes();
				TextNode tnode = (TextNode)childNodes.get(1);
				finalExamSlots.add(tnode.text().trim());
			}
		}
		//bDepartment.setFinalExamSlots(finalExamSlots);
	}

	private void parseCourseSlots(TDepartment bDepartment, Element element) {
		Elements tdElements = element.getElementsByTag("td");
		List<String> slots = new LinkedList<String>();
		if (tdElements.size() > 1) { 
			for (int index = 1; index < tdElements.size(); ++index) {
				Element slotElement = tdElements.get(index);
				List<Node> childNodes = slotElement.childNodes();
				TextNode tnode = (TextNode)childNodes.get(1);
				slots.add(tnode.text().trim());
			}
		}
//		bDepartment.setCourseSlots(slots);
	}

	private void parseCourseSemesterAndDepartment(TDepartment bDepartment, Element element) {
		Elements trEelements = element.getElementsByTag("td");
		
		String semester = trEelements.get(1).text();
//		bDepartment.setSemester(TParserHelper.getDefaultOrParsedPart(semester,TAdaptorConstants.DEFAULT_SEMESTER_VALUE));
		
		String department = trEelements.get(3).text();
//		bDepartment.setDepartmentCode(TParserHelper.getDefaultOrParsedPart(department,TAdaptorConstants.DEFAULT_DEPARTMENT_VALUE));
	}

}
