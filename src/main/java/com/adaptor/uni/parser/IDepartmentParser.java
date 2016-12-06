package com.adaptor.uni.parser;

import com.adaptor.uni.model.IDepartment;
import com.adaptor.uni.model.TDepartment;

public interface IDepartmentParser extends IParser{
	
	/**
	 * This method is used for parsing school web sites.
	 *
	 * @param departmentUrl the department url
	 * @param department the department
	 * @return the  department
	 */
	IDepartment parse(String departmentUrl,TDepartment department);
}
