package com.sio.model;

import java.nio.ByteBuffer;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sio.net.DefaultUDPTransceiver;

/**
 * 继承了Observable的解释器，可以把接收到数据转化并通知下去。
 * @author S
 *
 */
public class DataReader extends Observable{
	private static final int THREAD_LIMIT = 1;
	private static final int A4_PACK_PROTOCAL_LENGTH = 14;
	public static final byte HANDSET_PACK_PROTOCAL_HEAD = (byte) 0x02;
	public static final byte A4_PACK_PROTOCAL_HEAD = (byte) 0xA4;
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
	 * @return ip address
	 */
	public String getSrc_ip() {
		return src_ip;
	}
	/**
	 * JavaBean ip setter
	 * @param src_ip ip address
	 */
	public void setSrc_ip(String src_ip) {
		this.src_ip = src_ip;
	}
	/**
	 * JavaBean port number getter. 
	 * DO NOT SUGGEST TO CALL.
	 * PLEASE GET FROM Interpreter class.
	 * @return port
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
	 * @param data raw data
	 */
	public void setData(byte[] data) {
		String ip = src_ip;
		int port = src_port;
		this.data = data;
		
		if(data[0] == A4_PACK_PROTOCAL_HEAD){
			if((data.length-1) % A4_PACK_PROTOCAL_LENGTH == 0){
				for(int x=1; x<data.length; x+=A4_PACK_PROTOCAL_LENGTH){
					ByteBuffer buf = ByteBuffer.allocate(A4_PACK_PROTOCAL_LENGTH);
					buf.put(data,x,A4_PACK_PROTOCAL_LENGTH);
					GeneratingTag genTag = new GeneratingTag();
					genTag.setIp(ip);
					genTag.setPort(port);
					genTag.setData(buf.array());
					threadPool.execute(genTag);
				}
			}
		} else if (HANDSET_PACK_PROTOCAL_HEAD == data[0]) {
			if(data.length>7){
				int pack_count = 0;		//total count of pack
				int pack_index = 0;		//current index of pack
				{
					pack_count = data[1] & 0xFF;
					pack_count <<= 8;
					pack_count |= data[2] & 0xFF;
					pack_index = data[3] & 0xFF;
					pack_index <<= 8;
					pack_index |= data[4] & 0xFF;
				}
				System.out.println("device echo：" + Packer.fromBytesTo16radix(data));
				
				{
					ByteBuffer buff = ByteBuffer.allocate(1024);
					buff.put(data, 5, data.length-5);
					buff.flip();
					for(int x=0; x<buff.limit();){
						int product_num_length = buff.get() & 0xFF;
						int mac_length = buff.get() & 0xFF;
						buff.get();
						byte[] product_id = new byte[product_num_length];
						byte[] mac = new byte[mac_length];
						buff.get(product_id);
						buff.get(mac);
						System.out.println("inbuf p echo：" + Packer.fromBytesTo16radix(product_id));
						System.out.println("inbuf m echo：" + Packer.fromBytesTo16radix(mac));
						x += product_num_length + mac_length + 1 + 2;
					}
					
				}
				
				
			}
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
			interpreter.update(getIp(), getPort(), getData());
			setChanged();
			notifyObservers(interpreter.getPrototype());
		}

		private String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		private int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}
		
		private byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}
	}
}
