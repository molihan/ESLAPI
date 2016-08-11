package com.sio.util;

import com.sio.model.net.UDPTransceiver;


public interface UDPConnectionFactory {
	/**
	 * This function return's a object re-wrote by sub-class. 
	 * @param ip An IP address indicate to a specific net and route.
	 * @return UDPTransceiver object.
	 */
	public UDPTransceiver createUDPTransceiver(String ip);
	
}
