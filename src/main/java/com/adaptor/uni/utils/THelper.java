/**
 * 
 */
package com.adaptor.uni.utils;

/**
 * @author emin.paca
 *
 */
public class THelper {
	
	public static int castToInt(String intStr) {
		try {
			return Integer.parseInt(intStr);
		} catch(NumberFormatException ex) {
			return 0;
		}
	}

}
