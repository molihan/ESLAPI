package com.sio.model;

import java.util.Observable;

/**
 * �̳���Observable�Ľ����������԰ѽ��յ�����ת����֪ͨ��ȥ��
 * @author S
 *
 */
public class DataReader extends Observable{
	private byte[] data;
	private String src_ip;
	private int src_port;
	private Interpreter interpreter;
	
	public DataReader() {

	}

	public String getSrc_ip() {
		return src_ip;
	}

	public void setSrc_ip(String src_ip) {
		this.src_ip = src_ip;
	}

	public int getSrc_port() {
		return src_port;
	}

	public void setSrc_port(int src_port) {
		this.src_port = src_port;
	}
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
		interpreter.setType(Interpreter.TYPE_TAG);
		interpreter.update(data);
		setChanged();
	}

	public Interpreter getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(Interpreter interpreter) {
		this.interpreter = interpreter;
	}
	
}
