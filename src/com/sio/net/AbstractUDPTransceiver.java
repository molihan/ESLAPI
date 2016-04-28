package com.sio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;

public abstract class AbstractUDPTransceiver implements UDPTransceiver{
	private static final String IOEXCEPTION_UNINITIALED_CHANNEL = "DatagramChannel not initialized";
	protected final String BROADCAST_ADDRESS = "255.255.255.255";
	
	protected DatagramChannel datagramChannel = initialChannelHook();
	protected Selector selector = initialSelectorHook();
	protected boolean isRunning;
	
	/**
	 * CHN:����һ�����Ӻ�����������������д����ɳ�ʼ���������ֶ�����
	 * @return datagramChannel ����ʼ������ᱻreturn�Ķ��󸲸ǡ�
	 */
	protected abstract DatagramChannel initialChannelHook();
	/**
	 * CHN:����һ�����Ӻ�����������������д����ɳ�ʼ���������ֶ�����
	 * @return selector ����ʼ������ᱻreturn�Ķ��󸲸ǡ�
	 */
	protected abstract Selector initialSelectorHook();
	/**
	 * CHN:ע���ע�¼���
	 */
	protected void registration(int selectionKey){
		try {
			datagramChannel.register(selector, selectionKey);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}
	}
	/**
	 * CHN:��д�ú����Ը������ҵ���߼����ţ�ͬʱ������������
	 */
	protected abstract void onEventCallBack(DatagramChannel datagramChannel, Selector selector);
	/**
	 * CHN:����һ���̣߳������Ե��� function void onEventCallBack(DatagramChannel, Selector)����������ͨ������stopUDPEvent()��ֹͣ�����̡߳�
	 * 
	 */
	@Override
	public final void startUDPEvent(){
		if(!isRunning){
			isRunning = true;
			new Thread(new EventRunnable()).start();
		}
		
	}
	/**
	 * CHN��ֹͣ��ǰ�����̡߳�
	 */
	@Override
	public final void stopUDPEvent(){
		isRunning = false;
	}
	/**
	 * �ڲ��߳���
	 * @author S
	 *
	 */
	private class EventRunnable implements Runnable{
		private static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY;
		@Override
		public void run() {
			Thread.currentThread().setPriority(DEFAULT_THREAD_PRIORITY);
			while(isRunning){
				onEventCallBack(datagramChannel, selector);
			}
		}
	}
	
	/**
	 * ͨ��UDPChannel �� �������ݿ鷢���� Ŀ���ַ���˿�
	 * @param ip Ŀ��IP��ַ
	 * @param port Ŀ��˿ں�
	 * @param data ��������
	 * @throws IOException ��DatagramChnnel==null��δ���ó�ʼ������initialChannelHook()���׳����쳣����ʼ��������ͨ������������ֶ����á�
	 */
	@Override
	public void write(String ip, int port, byte[] data) throws IOException {
		if(datagramChannel == null){
			throw new IOException(IOEXCEPTION_UNINITIALED_CHANNEL);
		}
		try {
			datagramChannel.send(ByteBuffer.wrap(data), new InetSocketAddress(ip, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * ͨ��UDPChannel �� �������ݿ�㲥�� Ŀ��˿�
	 * @param port Ŀ��˿ں�
	 * @param data ��������
	 */
	@Override
	public void broadCast(int port, byte[] data) throws IOException{
		if(datagramChannel == null){
			throw new IOException(IOEXCEPTION_UNINITIALED_CHANNEL);
		}
		try {
			datagramChannel.send(ByteBuffer.wrap(data), new InetSocketAddress(BROADCAST_ADDRESS, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
