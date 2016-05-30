package com.sio.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sio.model.DefaultUDPAtomicData;

public class DefaultA1UDPTransceiver extends AbstractUDPTransceiver {
	private static final boolean _DEBUG_ = false;
	private static final int BUFFER_DEFAULT_SIZE = 1024;
	private static final int SECOND_IN_MILLIS = 1000;
	private static final int _COM_PORT_ = 15167;
	private boolean isFirstPack = true;
//	A1 resend
	private static final long A1_RESEND_IN_MILLIS = (long) (0.5*SECOND_IN_MILLIS);
	
	private ByteBuffer buf = ByteBuffer.allocate(BUFFER_DEFAULT_SIZE);
	private List<DefaultUDPAtomicData> queue = new ArrayList<>();
//	logger
//	private static final Logger logger = Logger.getLogger(DefaultA1UDPTransceiver.class);
	
	public DefaultA1UDPTransceiver() {
		
	}

	@Override
	protected DatagramChannel initialChannelHook() {
		DatagramChannel channel = null;
		String standard_ip = DefaultUDPTransceiver.standard_ip;
		
		channel = getFreeChannel(standard_ip);
		if(_DEBUG_){
			System.out.println("found free port: " + " @ip -> " + standard_ip);
			System.out.println("################################START#######################" + new Date());
		}
		
		try {
			channel.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
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
		if(isFirstPack){
			registration(SELECTION_WRITE);
			isFirstPack = false;
		}
		
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
			if(_DEBUG_){
				System.out.println("################################STOP#######################");
			}
		}
	}

	private void send_a1() throws IOException {
		if(queue.size()>0){
			DefaultUDPAtomicData pack = queue.get(0);
			write(pack.getIp(), pack.getPort(), pack.getData());
			registration(SELECTION_READ);
		}
	}

	private void read(DatagramChannel channel) throws IOException {
		buf.clear();
		channel.receive(buf);
		buf.flip();
		byte[] data = new byte[buf.remaining()];
		buf.get(data);
		queue.remove(0);
		registration(SELECTION_WRITE);
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
