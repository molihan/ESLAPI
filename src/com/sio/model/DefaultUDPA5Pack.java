package com.sio.model;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

public class DefaultUDPA5Pack extends Packer {
	public static final byte COMM_FLAG = (byte) 0xA5;
	private static final int A5_PACK_PROTPCAL_LENGTH = 7;
	public DefaultUDPA5Pack() {
		
	}

	@Override
	public void setHead(String mac, long random, Date time) {

	}

	@Override
	public void setData(byte order, byte[] data) {
		
	}

	public byte[] generatePack(int port){
		ByteBuffer buf = ByteBuffer.allocate(A5_PACK_PROTPCAL_LENGTH);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int dof = calendar.get(Calendar.DAY_OF_WEEK);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		buf.put((byte) (year % 1000 % 100));
		buf.put((byte) (month+1&0xff));
		switch(dof){
		case Calendar.MONDAY:
			buf.put((byte)1);
			break;
		case Calendar.TUESDAY:
			buf.put((byte)2);
			break;
		case Calendar.WEDNESDAY:
			buf.put((byte)3);
			break;
		case Calendar.THURSDAY:
			buf.put((byte)4);
			break;
		case Calendar.FRIDAY:
			buf.put((byte)5);
			break;
		case Calendar.SATURDAY:
			buf.put((byte)6);
			break;
		case Calendar.SUNDAY:
			buf.put((byte)7);
			break;
		}
		buf.put((byte) day);
		buf.put((byte)hour);
		buf.put((byte)minute);
		buf.put((byte)second);
		data = buf.array();
		return data;
	}
}
