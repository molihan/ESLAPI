package com.sio.model;
/**
 * Java Bean
 * Stock sending data.
 * @author S
 *
 */
public interface AtomicData {

	public void setData(byte[] data);
	public byte[] getData();
	public void setDescription(String desc);
	public String getDescription();
	
}
