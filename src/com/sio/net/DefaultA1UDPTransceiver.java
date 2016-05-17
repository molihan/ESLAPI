package com.sio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import com.sio.model.DefaultUDPAtomicData;

public class DefaultA1UDPTransceiver extends AbstractUDPTransceiver {
	private static final boolean _DEBUG_ = true;
	private static final int INVALID_ARG_FLAG = -1;
	private static final String UDP_PORT_OCCUPIED_ERROR = "ALL UDP PORT IS OCCUPIED.";
	private static final int BUFFER_DEFAULT_SIZE = 1024;
	private static final int SECOND_IN_MILLIS = 1000;
	private static final int _COM_PORT_ = 15167;
//	A1 resend
	private static final long A1_RESEND_IN_MILLIS = (long) (0.5*SECOND_IN_MILLIS);
	
	private ByteBuffer buf = ByteBuffer.allocate(BUFFER_DEFAULT_SIZE);
	private int standard_port = INVALID_ARG_FLAG;
	private List<DefaultUDPAtomicData> queue = new ArrayList<>();
//	logger
	private static final Logger logger = Logger.getLogger(DefaultA1UDPTransceiver.class);
	
	public DefaultA1UDPTransceiver() {
		registration(SELECTION_WRITE);
	}

	@Override
	protected DatagramChannel initialChannelHook() {
		DatagramChannel channel = null;
		try {
			channel = DatagramChannel.open();
			standard_port = getFreePort();
			channel.bind(new InetSocketAddress(standard_port));
			channel.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(standard_port == INVALID_ARG_FLAG){
			logger.error(UDP_PORT_OCCUPIED_ERROR);
			System.exit(INVALID_ARG_FLAG);
		}
		if(_DEBUG_){
			System.out.println("send udp is on port " + standard_port);
		}
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
			Selector selector) {
		if(queue.size()>0){
			try {
//				如果一秒没收到a1则重发
				if(selector.select(A1_RESEND_IN_MILLIS)>0){
					Set<SelectionKey> keys = selector.selectedKeys();
					for(SelectionKey key : keys){
						DatagramChannel channel = (DatagramChannel) key.channel();
						if(key.isReadable()){
							read(channel);
						} else if (key.isWritable()){
							send_a1();
						}
						keys.remove(key);
					}
				} else {
					send_a1();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			stopUDPEvent();
		}
	}

	private void send_a1() throws IOException {
		if(queue.size()>0){
			DefaultUDPAtomicData pack = queue.get(0);
			write(pack.getIp(), pack.getPort(), pack.getData());
		}
		registration(SELECTION_READ);
	}

	private void read(DatagramChannel channel) throws IOException {
		buf.clear();
		channel.receive(buf);
		buf.flip();
		byte[] data = new byte[buf.remaining()];
		buf.get(data);
		queue.remove(0);
	}
	
	public void setQueue(List<DefaultUDPAtomicData> queue){
		this.queue = queue;
	}

	public void addSendPacket(String ip, byte[]data){
		DefaultUDPAtomicData aData = new DefaultUDPAtomicData();
		aData.setIp(ip);
		aData.setPort(_COM_PORT_);
		aData.setSendType(DefaultUDPAtomicData.SEND_UDP);
		aData.setData(data);
		queue.add(aData);
	}
}
