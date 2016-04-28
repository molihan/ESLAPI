package com.sio.model;

public class InterpretedTag implements Tag{

	private String mac;
	private int model;
	private int signal;
	private String apIP;
	private int battary;
	private long code_1;
	private int code_2;
	private boolean error;
	private boolean on;
	
	@Override
	public String mac() {
		return mac;
	}

	@Override
	public int model() {
		return model;
	}

	@Override
	public int signal() {
		return signal;
	}

	@Override
	public String apIP() {
		return apIP;
	}

	@Override
	public int battary() {
		return battary;
	}

	@Override
	public long code_1() {
		return code_1;
	}

	@Override
	public int code_2() {
		return code_2;
	}

	@Override
	public boolean error() {
		return error;
	}

	@Override
	public boolean on() {
		return on;
	}


	
}
