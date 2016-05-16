package com.sio.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.sio.model.AtomicData;
import com.sio.model.DataQueue;
import com.sio.model.DataReader;
import com.sio.model.DefaultPackSheer;
import com.sio.model.DefaultUDPA5Pack;
import com.sio.model.DefaultUDPAtomicData;

public class DefaultUDPTransceiver extends AbstractUDPTransceiver {
	private static final int DEFAULT_BUFFER_SIZE = 1024;
	private static final int SECOND_IN_MILLIS = 1000;
	private static final int A5_REND = 5;
	private static final String UDP_PORT_OCCUPIED_ERROR = "ALL UDP PORT IS OCCUPIED.";
	private static final int INVALID_ARG_FLAG = -1;
	private static final int _COM_PORT_ = 15167;
	private static String standard_ip;
	private int standard_port = INVALID_ARG_FLAG;
	private long last_a5_pack;

	private DataQueue queue;
	private DataReader observable;
	private ByteBuffer receive_buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
//	Logger
	private static final Logger logger = Logger.getLogger(DefaultUDPTransceiver.class);
//	Properties
	private static final Properties props = new Properties();
	private static final File props_file = new File("./net.ini");
	private static final String KEY_IP = "ip";
	
	static {
		if(props_file.exists()){
			try {
				props.load(new FileReader(props_file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				props_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String ip = props.getProperty(KEY_IP);
		if(ip == null || ip.length() < 7){
			try {
				standard_ip = Inet4Address.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			standard_ip = ip;
		}
	}
	
	public DefaultUDPTransceiver() {
	
	}

	@Override
	protected DatagramChannel initialChannelHook() {
		DatagramChannel channel = null;
		try {
			channel = DatagramChannel.open();
			standard_port = getFreePort();
			channel.bind(new InetSocketAddress(standard_ip,standard_port));
			channel.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(standard_port == INVALID_ARG_FLAG){
			logger.error(UDP_PORT_OCCUPIED_ERROR);
			System.exit(INVALID_ARG_FLAG);
		}
		System.out.println("an UDP listen at " + standard_ip + " with port " + standard_port + ".");
		return channel;
	}

	@Override
	protected Selector initialSelectorHook() {
		Selector selector = null;
		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return selector;
	}

	@Override
	protected void onEventCallBack(DatagramChannel datagramChannel,
			Selector selector){
//		Judgment 
		if(queue != null && queue.hasRemaining()){
			registration(SELECTION_WRITE);
			
		} else {
			registration(SELECTION_READ);
			
		}
		
		try {
			if(selector.select(100)>0){
				Set<SelectionKey> keys = selector.selectedKeys();
				for(SelectionKey key : keys){
					if(key.isReadable()){
						read(key.channel());
					} else if(key.isWritable() && queue.hasRemaining()){
						AtomicData ad = queue.drillAtomicData();
						if(ad instanceof DefaultUDPAtomicData){
							switch(((DefaultUDPAtomicData) ad).getSendType()){
								case DefaultUDPAtomicData.SEND_UDP:
									write(((DefaultUDPAtomicData) ad).getIp(), ((DefaultUDPAtomicData) ad).getPort(), ad.getData());
									break;
								case DefaultUDPAtomicData.BROADCAST_UDP:
									broadCast(((DefaultUDPAtomicData) ad).getPort(), ad.getData());
									break;
								case DefaultUDPAtomicData.MULTICAST_UDP:
									broadCast(((DefaultUDPAtomicData) ad).getPort(), ad.getData());
									break;
								default:
									break;
							}
						}
					}
					keys.remove(key);
				}
			} else {
				spareTime();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void spareTime() {
		if(System.currentTimeMillis() - last_a5_pack >= A5_REND * SECOND_IN_MILLIS){
			DefaultUDPAtomicData data = new DefaultUDPAtomicData();
			try {
				data.setIp(Inet4Address.getLocalHost().getHostAddress());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			data.setPort(_COM_PORT_);
			data.setSendType(DefaultUDPAtomicData.BROADCAST_UDP);
			DefaultUDPA5Pack a5_pack = new DefaultUDPA5Pack();
			a5_pack.generatePack(standard_port);
			byte[] pack = a5_pack.getData();
			DefaultPackSheer sheer = new DefaultPackSheer();
			sheer.putData(DefaultUDPA5Pack.COMM_FLAG,pack);
			while(sheer.hasNext()){
				data.setData(sheer.getPack());
			}
			queue.putData(data);
			last_a5_pack = System.currentTimeMillis();
		}
	}

	/**
	 * UDP read data hook. This will called when readable.
	 * @param channel DatagramChannel occured readable event.
	 */
	protected void read(Channel channel){
		DatagramChannel dc = (DatagramChannel) channel;
		try {
			receive_buffer.clear();
			InetSocketAddress address = (InetSocketAddress) dc.receive(receive_buffer);
			String ip = address.getAddress().getHostAddress();
			int port = address.getPort();
			receive_buffer.flip();
			byte[] data = new byte[receive_buffer.remaining()];
			receive_buffer.get(data);
			observable.setSrc_ip(ip);
			observable.setSrc_port(port);
			observable.setData(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public DataQueue getQueue() {
		return queue;
	}

	public void setQueue(DataQueue queue) {
		this.queue = queue;
	}

	public DataReader getObservable() {
		return observable;
	}

	public void setObservable(DataReader observable) {
		this.observable = observable;
	}

}
