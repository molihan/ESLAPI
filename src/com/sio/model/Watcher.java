package com.sio.model;

/**
 * This class is use for pick out the offline tags and APs
 * @author S
 *
 */
public interface Watcher extends Runnable {
	/**
	 * Every time an AP made an report of tag should sign once.
	 * To keep it refresh at accurate recorded.
	 * @param prisoner String object represented for specific entity.
	 */
	public void sign(String prisoner);
	/**
	 * Filter out the dead tag and un_calling AP. 
	 */
	public void clear();
	
}
