package com.sio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

import com.sio.model.AtomicData;
import com.sio.model.DataQueue;
import com.sio.model.DataReader;
import com.sio.model.DefaultUDPAtomicData;

public class DefaultUDPTransceiver extends AbstractUDPTransceiver {
	private static final int DEFAULT_BUFFER_SIZE = 1024;
//	private static final int _COM_PORT = 15167;
	private static final int STANDING_PORT = 19999;

	private DataQueue queue;
	private DataReader observable;
	private ByteBuffer receive_buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
	public DefaultUDPTransceiver() {
		registration(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}

	@Override
	protected DatagramChannel initialChannelHook() {
		DatagramChannel channel = null;
		try {
			channel = DatagramChannel.open();
			channel.bind(new InetSocketAddress(STANDING_PORT));
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
			Selector selector){
		try {
			selector.select();
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
							case DefaultUDPAtomicData.MULTICAST_UDP:
								broadCast(((DefaultUDPAtomicData) ad).getPort(), ad.getData());
								break;
							default:
								break;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
			System.out.println(ip + " :" + port + " -> " + receive_buffer.array());
			observable.setSrc_ip(ip);
			observable.setSrc_port(port);
			observable.setData(receive_buffer.array());
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
