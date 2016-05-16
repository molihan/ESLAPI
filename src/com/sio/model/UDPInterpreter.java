package com.sio.model;

import java.nio.ByteBuffer;

public class UDPInterpreter implements Interpreter{
	
	private int type;
	private Object obj;
	
	@Override
	public Object getPrototype() {
		return obj;
	}
	
	@Override
	public void setPrototype(Object obj) {
		this.obj = obj;
	}
	
	@Override
	public int getType() {
		return type;
	}
	
	@Override
	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void update(String ip, int port, byte[] data) {
		InterpretedTag iTag = new InterpretedTag();
//		ip
		iTag.apIP = ip;
//		mac
		ByteBuffer buf = ByteBuffer.allocate(6);
		buf.put(data,MAC_6,6);
		iTag.mac = Packer.fromBytesTo16radix(buf.array());
//		battary
		byte status = data[BATTARY];
		int onOff = status & 0b1000_0000;
		onOff >>= 7;
		int err = status & 0b0100_0000;
		err >>= 6;
		int battary = status & 0b0011_1111;
		iTag.on = onOff == 1;
		iTag.error = err == 1;
		iTag.battary = battary*2;
//		product code 1
		int _code_ = 0;
		for(int x=0; x<3; x++){
			_code_ = data[PRODUCT_CODE_H + x];
			if(x<2)	_code_ <<= 8;
		}
		iTag.code_1 = _code_;
//		broadcast
		iTag.code_2 = data[INTVAL_TIME];
//		type
		iTag.model = data[LCDTYPE];
//		dbm
		iTag.signal = data[SIGNAL];
		
		setPrototype(iTag);
	}
	
}
