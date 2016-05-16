package com.sio.model;

import java.nio.ByteBuffer;

public class DefaultPackSheer implements PackSheer {
	private static final byte _HEAD= (byte) 0xFE;
	public static final byte SEND_PROTOCAL = (byte) 0xA1;
	private int raw_size = 0;
	private int total_pack = 0;
	private int current_pack = 0;
	byte flag;
	byte[] raw;
	
	public DefaultPackSheer() {
		
	}

	@Override
	public boolean hasNext() {
		return current_pack<total_pack;
	}

	@Override
	public byte[] getPack() {
		int predict_this_size = 0;
		int start_position = current_pack*DEFAULT_PACK_SIZE;
		ByteBuffer buf = ByteBuffer.allocate(DEFAULT_HEAD_SIZE+DEFAULT_PACK_SIZE);
		buf.put(_HEAD);
//		current pack length
		if(current_pack<total_pack-1){
			predict_this_size = 512;
		}else {
			predict_this_size = raw_size % 512;
		}
		for(int x=2; x>=0; x--){
			byte length = (byte) ((predict_this_size >> (8*x) ) & 0xFF);
			buf.put(length);
		}
//		flag
		buf.put(flag);
//		total pack count
		for(int x=1; x>=0; x--){
			byte total_count = (byte) ((total_pack >> (8*x) ) & 0xFF);
			buf.put(total_count);
		}
//		current pack count
		for(int x=1; x>=0; x--){
			byte total_count = (byte) ((current_pack >> (8*x) ) & 0xFF);
			buf.put(total_count);
		}
		
//		put raw data
		
		buf.put(raw, start_position, predict_this_size);
		current_pack++;
		byte[] dst = new byte[buf.flip().remaining()];
		buf.get(dst);
		return dst;
	}

	@Override
	public void reset() {
		
		if(raw_size <= DEFAULT_PACK_SIZE){
			total_pack = 1;
		} else {
			if(raw_size % DEFAULT_PACK_SIZE == 0){
				total_pack = raw_size / DEFAULT_PACK_SIZE;
			} else {
				total_pack = raw_size / DEFAULT_PACK_SIZE;
				total_pack += 1;
			}
		}
		
	}

	@Override
	public void putData(byte flag,byte[] raw) {
		this.flag = flag;
		this.raw = raw;
		raw_size = raw.length;
		reset();
	}

}
