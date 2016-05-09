package com.sio.model;

import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 继承了Observable的解释器，可以把接收到数据转化并通知下去。
 * @author S
 *
 */
public class DataReader extends Observable{
	private static final int THREAD_LIMIT = 1;
	private static final ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_LIMIT);

	private String src_ip;
	private int src_port;
	private byte[] data;
	private Interpreter interpreter;
	
	public DataReader() {

	}
	/**
	 * JavaBean ip getter
	 * DO NOT SUGGEST TO CALL.
	 * PLEASE GET FROM Interpreter class.
	 * @return
	 */
	public String getSrc_ip() {
		return src_ip;
	}
	/**
	 * JavaBean ip setter
	 * @param src_ip
	 */
	public void setSrc_ip(String src_ip) {
		this.src_ip = src_ip;
	}
	/**
	 * JavaBean port number getter. 
	 * DO NOT SUGGEST TO CALL.
	 * PLEASE GET FROM Interpreter class.
	 * @return
	 */
	public int getSrc_port() {
		return src_port;
	}

	public void setSrc_port(int src_port) {
		this.src_port = src_port;
	}
	
	public byte[] getData() {
		return data;
	}
	/**
	 * Raw data
	 * @param data
	 */
	public void setData(byte[] data) {
		String ip = src_ip;
		int port = src_port;
		this.data = data;
		for(int x=0; x<data.length; x++){
			GeneratingTag genTag = new GeneratingTag();
			genTag.setIp(ip);
			genTag.setPort(port);
			genTag.setData(data);
			threadPool.execute(genTag);
		}
	}

	public Interpreter getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(Interpreter interpreter) {
		this.interpreter = interpreter;
	}
	
	private class GeneratingTag implements Runnable{
		private String ip;
		private int port;
		private byte[] data;
		
		@Override
		public void run() {
			interpreter.setType(Interpreter.TYPE_TAG);
			interpreter.update(data);
			notifyObservers(interpreter);
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
		
		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}
	}
}
