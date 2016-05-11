package com.sio.model;

import java.util.Date;

public abstract class Packaging {
	protected static final int HEAD_DEFAULT_LENGTH = 48;
	protected static final byte PRODUCT_CODE_H = 0;
	protected static final byte PRODUCT_CODE_M = 1;
	protected static final byte PRODUCT_CODE_L = 2;
	protected static final byte TOTAL_BYTE_COUNT_H = 3;
	protected static final int TOTAL_BYTE_COUNT_M = 4;
	protected static final int TOTAL_BYTE_COUNT_L = 5;
	protected static final int MAC_6 = 8;
	protected static final int MAC_5 = 9;
	protected static final int MAC_4 = 10;
	protected static final int MAC_3 = 11;
	protected static final int MAC_2 = 12;
	protected static final int MAC_1 = 13;
	protected static final int COMMAND_PROTOCAL_FLAG = 14;
	protected static final int YEAR = 16;
	protected static final int MONTH = 17;
	protected static final int DOF = 18;
	protected static final int DAY = 19;
	protected static final int HOUR = 20;
	protected static final int MINUTE = 21;
	protected static final int SECOND = 22;
	protected static final int COMMAND_FIRST_FLAG = 48;
	protected static final int COMMAND_SECON_FLAG = 52;
	protected static final int COMMAND_THIRD_FLAG = 56;
	protected static final int COMMAND_FORTH_FLAG = 60;
	protected static final byte _EMPTY_ = (byte)0xFF;
	protected static final int COMMAND_ROW_LENGTH = 16;
	protected static final byte[] COMMAND_ROW_INIT = new byte[]{(byte) 0xFF,0x00,0x00,0x00,(byte) 0xFF,0x00,0x00,0x00,(byte) 0xFF,0x00,0x00,0x00,(byte) 0xFF,0x00,0x00,0x00,};
	/**
	 * 每四个指令，如果指令没结束就把0x80与上指令。
	 */
	protected static final byte ORDER_MASK = (byte) 0x80;
	protected static final byte ORDER_SEND_BW = 0x00;
	protected static final byte ORDER_SEND_R = 0x01;
	protected static final byte ORDER_SEND_G = 0x02;
	protected static final byte ORDER_LED = 0x10;
	protected static final byte ORDER_SLEEP = 0x20;
	protected static final byte ORDER_BUTTON = 0x30;
	protected static final byte ORDER_RESET = 0x40;
	
	protected byte[] head;
	protected byte[] data;
	public Packaging() {
		
	}
	
	public abstract void setHead(String mac, long random, Date time);
	public abstract void setData(byte[] data, byte order);
	
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
	
	public String fromBytesTo16radix(byte[] b){
		if (b != null && b.length > 0) {
			final StringBuilder stringBuilder = new StringBuilder(b.length);
				for(byte byteChar : b)
					stringBuilder.append(String.format("%02X ", byteChar));
	            return stringBuilder.toString();
		}
	    return null;
	}

	public byte[] getData(){
		return data;
	}
	
	public byte[] getHead(){
		return head;
	}
}
	
	
