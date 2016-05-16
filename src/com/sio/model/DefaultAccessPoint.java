package com.sio.model;

import com.sio.net.DefaultA1UDPTransceiver;


public class DefaultAccessPoint extends AbstractAccessPoint {
	private static final byte[] CLEAR_COMMAND = new byte[] {(byte) 0xFE,00,00,16,(byte) 0xB1,0x00,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte) 0xB1,0x00,};
	@Override
	public void clearCommand() {

		DefaultA1UDPTransceiver a1udp = new DefaultA1UDPTransceiver();

		a1udp.addSendPacket(getIp(), CLEAR_COMMAND);
		
		a1udp.startUDPEvent();

	}

	@Override
	public void sendCommand(byte[] command) {
		// TODO Auto-generated method stub

	}

	
}
