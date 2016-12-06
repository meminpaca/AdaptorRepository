/**
 * 
 */
package com.adaptor.uni.parser.bogazici;

import java.io.IOException;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.adaptor.uni.model.IUniversity;
import com.adaptor.uni.model.TDepartment;
import com.adaptor.uni.model.TUniversity;
import com.adaptor.uni.parser.AUniversityParser;
import com.adaptor.uni.utils.TBogaziciParameters;
import com.adaptor.uni.utils.TUniversityIDs;
import com.adaptor.uni.utils.TUniversityNames;

/**
 * The Class TBogaziciUniversityParser.
 *
 * @author emin.paca
 */
public class TBogaziciUniversityParser extends AUniversityParser {
	
	/**
	 * Logger for logging  events
	 * */
	public static  final Logger logger = LogManager.getLogger(TBogaziciUniversityParser.class);
	
	public IUniversity parse() {
		
		Document doc = null;
		IUniversity bogaziciUni = null;
		try {
			doc = Jsoup.connect(TBogaziciParameters.SCHEDULE_LIST_URL)
					  .data(TBogaziciParameters.POST_DATA_SEMESTER_NAME, 
							TBogaziciParameters.POST_DATA_SEMESTER_VALUE)
					  .post();
			bogaziciUni = new TUniversity(TUniversityNames.BOGAZICI_UNIVERSITY_NAME, 
											TUniversityNames.BOGAZICI_UNIVERSITY_NAME,
											TUniversityIDs.BOGAZICI_UNIVERSITY_ID,
											TBogaziciParameters.POST_DATA_SEMESTER_VALUE,
											TBogaziciParameters.SEMESTER_ID);
			
			parseDepartmentList(bogaziciUni,doc);
		} catch (IOException e) {
			logger.error("Getting Bogazici University schedule web site failed.", e);
		}
		return bogaziciUni;
	}
	
	/**
	 * @param doc
	 */
	private void parseDepartmentList(IUniversity university, Document doc) {
		Elements departmentElements = doc.getElementsByClass(TBogaziciParameters.DEPARTMENT_CLASS_SPECIFIER);
		if (departmentElements.size() > 0) {
			Iterator<Element> depIt = departmentElements.iterator();
			while (depIt.hasNext()){
				Element depElementTd = depIt.next();
				Element depElementA = depElementTd.select("a[href]").get(0);
				String departmentName = depElementA.text();
				String relativeUrl = depElementA.attr("href");
				// TODO remove if statement
				//if (!relativeUrl.contains("kisaadi=ASIA")) continue;
				
				String directDepartmentUrl = TBogaziciParameters.SCHEDULE_ROOT_URL + relativeUrl;
//				System.out.println(departmentName);
				TBogaziciDepartmentParser departmentParser = new TBogaziciDepartmentParser();
				TDepartment departmentObj = new TDepartment(departmentName,parseShortName(relativeUrl),1);

				departmentParser.parse(directDepartmentUrl,departmentObj);
				university.addDepartment(departmentObj);
				sleepForAWhile();
			}
			
		} else {
			logger.error("There is no department to be parsed.");
		}		
	}
	
	/**
	 * 
	 */
	private void sleepForAWhile() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String parseShortName(String s) {
		String startKeyword = "&kisaadi=";
		String endKeyword = "&bolum";
		int startIndex = s.indexOf(startKeyword);
		int endIndex = s.indexOf(endKeyword);
		String result = "";
		if (startIndex != -1 && endIndex != -1) {
			result = s.substring(startIndex + startKeyword.length(), endIndex);
		}
		return result;
		
	}

	

}
