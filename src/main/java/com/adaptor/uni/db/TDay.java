/**
 * 
 */
package com.adaptor.uni.db;

/**
 * @author emin.paca
 *
 */
public class TDay {
	
	private int id;
	private String name;
	
	
	/**
	 * @param id
	 * @param name
	 */
	public TDay(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the day
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the day to set
	 */
	public void setDay(String name) {
		this.name = name;
	}
	
	

}
