package com.sio.model;

import java.util.Date;

public abstract class Packaging {
	protected byte[] head;
	protected byte[] data;
	public Packaging() {
		
	}
	
	public abstract void setHead(String mac, long random, Date time);
	public abstract void setData(byte[] data, int order);
	
	protected final byte[] from16radixToBytes(String s) {
		byte[] b = new byte[s.length() / 2];
		int startpoint = 0;
		int endpoint = 2;
		for (int x = 0; x < b.length; x++) {
			b[x] = (byte) Integer.parseInt(s.substring(startpoint, endpoint),
					16);
			startpoint = endpoint;
			endpoint += 2;
		}
		return b;
	}
}
	
	
