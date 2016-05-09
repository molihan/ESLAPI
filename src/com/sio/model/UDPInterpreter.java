package com.sio.model;

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
	public void update(byte[] data) {
		
	}
	
}
