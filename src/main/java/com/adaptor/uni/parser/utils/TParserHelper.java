package com.adaptor.uni.parser.utils;

/**
 * The Class TParserHelper.
 */
public class TParserHelper {
	
	
	/**
	 * Gets the default or parsed part.
	 *
	 * @param target the target
	 * @param defaultVal the default value
	 * @return the default or parsed part
	 */
	public static String getDefaultOrParsedPart(String target, String defaultVal) {
		String result;
		if (target != null && !"".equals(target.trim().replace("\u00a0", ""))) {
			result = target.trim().replace("\u00a0", "");
		} else {
			result = defaultVal;
		}
		return result;
	}

}
