/**
 * 
 */
package com.adaptor.uni.parser;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.adaptor.uni.utils.TBogaziciParameters;

/**
 * @author emin.paca
 *
 */
public class Test {
	
	public static void main(String[] args) throws IOException {
		 Document doc = Jsoup.connect("https://oibs.metu.edu.tr/cgi-bin/Departments_Course_Schedule_102/Departments_Course_Schedule_102.cgi")
				  .data("input_Dept_Code", "236")
				  .data("input_Semester", "20161")
				  .data("SubmitName", "Course Schedule")
				  .data("SaFormName", "action_index__Findex_html")
				  // and other hidden fields which are being passed in post request.
				  .post();
				   System.out.println(doc); // will print html source of homepage of facebook.
	}

}
