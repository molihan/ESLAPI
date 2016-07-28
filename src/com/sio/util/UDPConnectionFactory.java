package com.sio.util;

import com.sio.model.net.UDPTransceiver;


public interface UDPConnectionFactory {
	/**
	 * This function return's a object re-wrote by sub-class. 
	 * @return UDPTransceiver object.
	 */
	public UDPTransceiver createUDPTransceiver();
	
}
