package com.sio.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public abstract class AbstractUDPTransceiver implements UDPTransceiver{
	protected static final int SELECTION_READ = SelectionKey.OP_READ;
	protected static final int SELECTION_WRITE = SelectionKey.OP_WRITE;
	private static final String IOEXCEPTION_UNINITIALED_CHANNEL = "DatagramChannel not initialized";
	protected final String BROADCAST_ADDRESS = "255.255.255.255";
	
	protected DatagramChannel datagramChannel;
	protected Selector selector;
	protected boolean isRunning;
	
	public AbstractUDPTransceiver() {
		datagramChannel = initialChannelHook();
		selector = initialSelectorHook();
	}
	
	/**
	 * CHN:这是一个钩子函数，必须在子类重写来完成初始化，无需手动调用
	 * @return datagramChannel 待初始化对象会被return的对象覆盖。
	 */
	protected abstract DatagramChannel initialChannelHook();
	/**
	 * CHN:这是一个钩子函数，必须在子类重写来完成初始化，无需手动调用
	 * @return selector 待初始化对象会被return的对象覆盖。
	 */
	protected abstract Selector initialSelectorHook();
	/**
	 * CHN:注册关注事件。
	 */
	protected void registration(int selectionKey){
		try {
			datagramChannel.register(selector, selectionKey);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}
	}
	/**
	 * CHN:重写该函数对该类进行业务逻辑编排，同时解析接收数据
	 */
	protected abstract void onEventCallBack(DatagramChannel datagramChannel, Selector selector);
	/**
	 * CHN:开启一个线程，周期性调用 function void onEventCallBack(DatagramChannel, Selector)方法。可以通过调用stopUDPEvent()来停止任务线程。
	 * 
	 */
	@Override
	public final void startUDPEvent(){
		if(!isRunning){
			isRunning = true;
			Thread t = new Thread(new EventRunnable());
			t.start();
			
		}
		
	}
	/**
	 * CHN：停止当前任务线程。
	 */
	@Override
	public final void stopUDPEvent(){
		isRunning = false;
		finalize();
	}
	/**
	 * 内部线程类
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
	 * 通过UDPChannel 将 传入数据块发送至 目标地址及端口
	 * @param ip 目标IP地址
	 * @param port 目标端口号
	 * @param data 发送内容
	 * @throws IOException 当DatagramChnnel==null从未调用初始化方法initialChannelHook()会抛出该异常。初始化方法在通常情况下无需手动调用。
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
	 * 通过UDPChannel 将 传入数据块广播至 目标端口
	 * @param port 目标端口号
	 * @param data 发送内容
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
	
	// 获取通信端口号
	protected int getFreePort() { 
		DatagramSocket s = null;//为UDP编程中的Socket类,只可以判断UDP占用的端口
		// 测试两个值之间的端口号
		int MINPORT = 2048;
		int MAXPORT = 65000;
	
		for (; MINPORT < MAXPORT; MINPORT+=8) {
	
			try {
				// 第二个为测试本机IP,测试其它机器,则构建一个InetAddress对象
				s = new DatagramSocket(MINPORT, InetAddress.getLocalHost());
				s.close();
				return MINPORT;
			} catch (IOException e) {
				// 如果报错就说明报错了,继续测试上面的.
				continue;
			}
	
		}
	
		// 如果都在用就返回-1
		return -1;
	}
	
	public void finalize(){
		try {
			datagramChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			selector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
