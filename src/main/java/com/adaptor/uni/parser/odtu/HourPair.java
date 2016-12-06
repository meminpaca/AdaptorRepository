/**
 * 
 */
package com.adaptor.uni.parser.odtu;

/**
 * @author emin.paca
 *
 */
public class HourPair {

	private int timeTableId;
	private String start;
	private String end;
	
	
	/**
	 * @param timeTableId
	 * @param start
	 * @param end
	 */
	public HourPair(int timeTableId, String start, String end) {
		super();
		this.timeTableId = timeTableId;
		this.start = start;
		this.end = end;
	}
	/**
	 * @return the timeTableId
	 */
	public int getTimeTableId() {
		return timeTableId;
	}
	/**
	 * @param timeTableId the timeTableId to set
	 */
	public void setTimeTableId(int timeTableId) {
		this.timeTableId = timeTableId;
	}
	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}
	
	
}
