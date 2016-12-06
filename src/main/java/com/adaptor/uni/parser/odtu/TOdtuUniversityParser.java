/**
 * 
 */
package com.adaptor.uni.parser.odtu;

import java.io.IOException;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.adaptor.uni.model.IDepartment;
import com.adaptor.uni.model.IUniversity;
import com.adaptor.uni.model.TDepartment;
import com.adaptor.uni.model.TUniversity;
import com.adaptor.uni.parser.AUniversityParser;
import com.adaptor.uni.parser.bogazici.TBogaziciDepartmentParser;
import com.adaptor.uni.utils.TBogaziciParameters;
import com.adaptor.uni.utils.TOdtuParameters;
import com.adaptor.uni.utils.TUniversityIDs;
import com.adaptor.uni.utils.TUniversityNames;

/**
 * @author emin.paca
 *
 */
public class TOdtuUniversityParser extends AUniversityParser {
	
	/**
	 * Logger for logging  events
	 * */
	public static  final Logger logger = LogManager.getLogger(TOdtuUniversityParser.class);
	

	/* (non-Javadoc)
	 * @see com.adaptor.uni.parser.IUniversityParser#parse()
	 */
	public IUniversity parse() {
		Document doc = null;
		IUniversity odtuUni = null;
		try {
			
			Connection conn = Jsoup.connect(TOdtuParameters.SCHEDULE_LIST_URL)
					.data(TOdtuParameters.INPUT_DEPT_CODE, TOdtuParameters.INPUT_DEPT_CODE_DEPARTMENT_LIST_VALUE)
					.data(TOdtuParameters.INPUT_SEMESTER, TOdtuParameters.INPUT_SEMESTER_DEPARTMENT_LIST_VALUE)
					.data(TOdtuParameters.SUBMIT_NAME, TOdtuParameters.SUBMIT_NAME_DEPARTMENT_LIST_VALUE)
					.data(TOdtuParameters.SA_FORM_NAME, TOdtuParameters.SA_FORM_NAME_DEPARTMENT_LIST_VALUE)
					.method(Connection.Method.POST);
			Response response = conn.execute();
			
			doc = Jsoup.parse(new String(response.bodyAsBytes(),"ISO-8859-9"));
			
			System.out.println(doc); // will print html source of homepage of facebook.
			odtuUni = new TUniversity(TUniversityNames.ODTU_UNIVERSITY_NAME, 
											TUniversityNames.ODTU_UNIVERSITY_NAME,
											TUniversityIDs.ODTU_UNIVERSITY_ID,
											TOdtuParameters.POST_DATA_SEMESTER_VALUE,
											TOdtuParameters.SEMESTER_ID);
			
			parseDepartmentList(odtuUni,doc);
		} catch (IOException e) {
			logger.error("Getting ODTU schedule web site failed.", e);
		}
		return odtuUni;
	}
	
	/**
	 * @param odtuUni
	 * @param doc
	 */
	private void parseDepartmentList(IUniversity odtuUni, Document doc) {
		Elements departmentElements = doc.select("table[border=1]");
		boolean isFirstRowSkipped = false;
		if (departmentElements.size() > 0) {
			Elements departmentsRows = departmentElements.select("tr");
			Iterator<Element> depIt = departmentsRows.iterator();
			while (depIt.hasNext()){
				Element depElementTr = depIt.next();
				if (!isFirstRowSkipped) {
					isFirstRowSkipped = true;
					continue;
				}
				Elements depElementTds = depElementTr.select("td");
				String progType = depElementTds.get(1).text();
				String progStatus = depElementTds.get(2).text();
				// skip courses other than Major
				if (!"Major".equals(progType)){
					continue;
				}
				
				if (!"Open".equals(progStatus)){
					continue;
				}
				boolean a = true;
				if (a)continue;
				
				String departmentShortName = depElementTds.get(4).text();
				String departmentNameEn = depElementTds.get(5).text();
				String departmentNameTr = depElementTds.get(6).text();
				String departmentCode = depElementTds.get(7).text();
				// TODO remove if statement
//				System.out.println(departmentName);
				TodtuDepartmentParser departmentParser = new TodtuDepartmentParser();
				TDepartment departmentObj = new TDepartment(departmentNameEn,departmentCode + "-" + departmentShortName,15);

				departmentParser.parse("",departmentObj);
				odtuUni.addDepartment(departmentObj);
				//sleepForAWhile();
			}
			
		} else {
			logger.error("There is no department to be parsed.");
		}		
	}
}
