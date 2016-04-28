package com.sio.model;


public class DefaultUDPAtomicData implements AtomicData {
	public static final int SEND_UDP = 0;
	public static final int BROADCAST_UDP = 1;
	public static final int MULTICAST_UDP = 2;
	
	private byte[] data;
	private String description;
	private String ip;
	private int port;
	private int sendType;
	

	public int getSendType() {
		return sendType;
	}
	public void setSendType(int sendType) {
		this.sendType = sendType;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	@Override
	public byte[] getData() {
		return data;
	}
	@Override
	public void setData(byte[] data) {
		this.data = data;
	}
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
}
