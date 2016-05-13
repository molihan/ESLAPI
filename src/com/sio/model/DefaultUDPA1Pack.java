package com.sio.model;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

public class DefaultUDPA1Pack extends Packer {

	@Override
	public void setHead(String mac, long random, Date time) {
		this.head = new byte[HEAD_DEFAULT_LENGTH];
//		block set product code
		{
			for(int x=PRODUCT_CODE_H; x<=PRODUCT_CODE_L; x++){
				int y = 2 - x;
				this.head[x] = (byte) ((random >> (y*8))& 0xFF);
			}
		}
//		block set mac
		{
			int i = 0;
			byte[] mac_bytes = from16radixToBytes(mac);
			for(int x=MAC_6; x<=MAC_1&i<mac_bytes.length; x++){
				this.head[x] = mac_bytes[i++];
			}
		}
//		block set time
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int dof = calendar.get(Calendar.DAY_OF_WEEK);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			this.head[YEAR] = (byte) (year % 1000 % 100);
			this.head[MONTH] = (byte) (month+1&0xff);
			switch(dof){
			case Calendar.MONDAY:
				this.head[DOF] = (byte)1;
				break;
			case Calendar.TUESDAY:
				this.head[DOF] = (byte)2;
				break;
			case Calendar.WEDNESDAY:
				this.head[DOF] = (byte)3;
				break;
			case Calendar.THURSDAY:
				this.head[DOF] = (byte)4;
				break;
			case Calendar.FRIDAY:
				this.head[DOF] = (byte)5;
				break;
			case Calendar.SATURDAY:
				this.head[DOF] = (byte)6;
				break;
			case Calendar.SUNDAY:
				this.head[DOF] = (byte)7;
				break;
			}
			this.head[DAY] = (byte) day;
			this.head[HOUR] = (byte)hour;
			this.head[MINUTE] = (byte)minute;
			this.head[SECOND] = (byte)second;
			
		}
		
//		initialize command flag
		{
			ByteBuffer buffer = ByteBuffer.allocate(HEAD_DEFAULT_LENGTH + COMMAND_ROW_LENGTH);
			buffer.put(head);
			buffer.put(COMMAND_ROW_INIT);
			this.head = buffer.array();
		}
	}
	public static final int ADDRESS_LENGTH = 3;
	@Override
	public void setData(byte order, byte[] data) {
//		edit head
		{
			if(head[head.length-4] != _EMPTY_ ){
				head[head.length-4] |= ORDER_MASK;
				ByteBuffer buffer = ByteBuffer.allocate(head.length+COMMAND_ROW_LENGTH);
				buffer.put(head);
				buffer.put(COMMAND_ROW_INIT);
				head = buffer.array();
			}
			for(int x=COMMAND_FIRST_FLAG; x<head.length; x+=4){
				if(this.head[x] == _EMPTY_){
					head[x] = order;
					int position = 0;
					if(this.data == null){
						position = 0;
						this.data = data;
					} else {
						position = this.data.length;
						ByteBuffer buffer = ByteBuffer.allocate(this.data.length + data.length);
						buffer.put(this.data);
						buffer.put(data);
						this.data = buffer.array();
					}

					for(int y=0; y<ADDRESS_LENGTH; y++){
						int i = ADDRESS_LENGTH-y-1;
						head[x+y+1] = (byte)((position >> (8*i))&0xFF); 
					}
					break;
				}
			}
		}
		
	}
	/**
	 * Merge
	 */
	private void merge(){
//		fix position (add head.length)
		{
			for(int x=COMMAND_FIRST_FLAG; x<head.length;x++){
				int position = 0;
				for(int y=0; y<ADDRESS_LENGTH; y++){
					x++;
					position |= head[x];
					if(y<ADDRESS_LENGTH-1)	position <<= 8;
				}
				position += head.length;
				for(int y=x-2; y<=x; y++){
					int i = ADDRESS_LENGTH-y-1;
					head[y] = (byte)((position >> (8*i))&0xFF); 
				}
			}
		}
	}
	
	public byte[] getByte(){
		merge();
		return null;
	}
}
