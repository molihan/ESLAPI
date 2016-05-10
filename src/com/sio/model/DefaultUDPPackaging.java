package com.sio.model;

import java.nio.ByteBuffer;
import java.util.Date;

public class DefaultUDPPackaging extends Packaging {
	private static final byte PRODUCT_CODE_H = 0;
	private static final byte PRODUCT_CODE_M = 1;
	private static final byte PRODUCT_CODE_L = 2;
	private static final byte TOTAL_BYTE_COUNT_H = 3;
	private static final int TOTAL_BYTE_COUNT_M = 4;
	private static final int TOTAL_BYTE_COUNT_L = 5;
	private static final int MAC_6 = 8;
	private static final int MAC_5 = 9;
	private static final int MAC_4 = 10;
	private static final int MAC_3 = 11;
	private static final int MAC_2 = 12;
	private static final int MAC_1 = 13;
	private static final int COMMAND_PROTOCAL_FLAG = 14;
	private static final int YEAR = 16;
	private static final int MONTH = 17;
	private static final int DOF = 18;
	private static final int DAY = 19;
	private static final int HOUR = 20;
	private static final int MINUTE = 21;
	private static final int SECOND = 22;
	private static final int COMMAND_FIRST_FLAG = 48;
	private static final int COMMAND_FIRST_ADD_H = 49;
	private static final int COMMAND_FIRST_ADD_M = 50;
	private static final int COMMAND_FIRST_ADD_L = 51;
	private static final int COMMAND_SECON_FLAG = 52;
	private static final int COMMAND_SECON_ADD_H = 53;
	private static final int COMMAND_SECON_ADD_M = 54;
	private static final int COMMAND_SECON_ADD_L = 55;
	private static final int COMMAND_THIRD_FLAG = 56;
	private static final int COMMAND_THIRD_ADD_H = 57;
	private static final int COMMAND_THIRD_ADD_M = 58;
	private static final int COMMAND_THIRD_ADD_L = 59;
	private static final int COMMAND_FORTH_FLAG = 60;
	private static final int COMMAND_FORTH_ADD_H = 61;
	private static final int COMMAND_FORTH_ADD_M = 62;
	private static final int COMMAND_FORTH_ADD_L = 63;
	
	public DefaultUDPPackaging() {
		
	}

	@Override
	public void setHead(String mac, long random, Date time) {
		this.head = new byte[64];
		//block set product code
		{
			for(int x=PRODUCT_CODE_H; x<=PRODUCT_CODE_L; x++){
				int y = 2 - x;
				this.head[x] = (byte) ((random >> (y*8))& 0xFF);
			}
		}
		//block set mac
		{
			int i = 0;
			byte[] mac_bytes = from16radixToBytes(mac);
			for(int x=MAC_6; x<=MAC_1&i<mac_bytes.length; x++){
				this.head[x] = mac_bytes[i++];
			}
		}
		//block set time
		{
			
		}
	}

	@Override
	public void setData(byte[] data, int order) {
		
	}
	/**
	 * Merge
	 */
	private void merge(){
		
	}
	
	public byte[] getByte(){
		merge();
		return null;
	}
}
