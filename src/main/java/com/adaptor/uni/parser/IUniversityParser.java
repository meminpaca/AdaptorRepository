/**
 * 
 */
package com.adaptor.uni.parser;

import com.adaptor.uni.model.IUniversity;

/**
 * The İnterface IUniversityParser.
 */
public interface IUniversityParser extends IParser{

	/**
	 * Parses university schedule sites.
	 *
	 * @return the university object
	 */
	IUniversity parse();
}
