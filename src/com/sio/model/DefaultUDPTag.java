package com.sio.model;

import com.sio.net.DefaultA1UDPTransceiver;

public class DefaultUDPTag extends WirelessTag {
	
	public DefaultUDPTag() {
		
	}

	/**
	 * <h3><b>DO NOT CALL/CREATE PackSheer YOURSELF.</b></h3>
	 * Directly use this function as default.
	 * 
	 * @param data All data will be trimmed automatically.
	 * 
	 */
	@Override
	public synchronized void write(byte[] data) {
		PackSheer sheer = new DefaultPackSheer();
		sheer.putData(DefaultPackSheer.SEND_PROTOCAL, data);
		DefaultA1UDPTransceiver a1udp = new DefaultA1UDPTransceiver();
		while(sheer.hasNext()){
			a1udp.addSendPacket(getTag().apIP(), sheer.getPack());
		}
		a1udp.startUDPEvent();
	}

}
