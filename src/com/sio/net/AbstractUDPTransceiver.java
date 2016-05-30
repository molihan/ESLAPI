package com.sio.net;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Enumeration;

import com.sio.model.Packer;

public abstract class AbstractUDPTransceiver implements UDPTransceiver{
	protected static final int SELECTION_READ = SelectionKey.OP_READ;
	protected static final int SELECTION_WRITE = SelectionKey.OP_WRITE;
	private static final String IOEXCEPTION_UNINITIALED_CHANNEL = "DatagramChannel not initialized";
	protected final String BROADCAST_ADDRESS = "255.255.255.255";
	protected DatagramChannel datagramChannel;
	protected Selector selector;
	protected boolean isRunning;
	
	public AbstractUDPTransceiver() {

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
	 * This function must call in onEventCallBack() method block
	 * <br> or after-ward.<br>
	 * <b>DO NOT CALL IN CONSTRUCTION AND INITIALHOOKS</b>
	 * CHN:注册关注事件。
	 */
	protected final void registration(int selectionKey){
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
	 * @param synchronize if true, will block, if false, not block.
	 * @return the thread of udp event.
	 */
	@Override
	public final Thread startUDPEvent(boolean synchronize){
		datagramChannel = initialChannelHook();
		selector = initialSelectorHook();
		
		Thread t = null;
		if(!isRunning){
			isRunning = true;
			t = new Thread(new EventRunnable());
			if(synchronize){
				try {
					t.start();
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				t.start();
			}
		}
		return t;
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
	protected int getFreePort(String ip) { 
		//为UDP编程中的Socket类,只可以判断UDP占用的端口
		// 测试两个值之间的端口号
		int MINPORT = 2048;
		int MAXPORT = 65000;
		for (; MINPORT < MAXPORT; MINPORT+=8) {
			// 第二个为测试本机IP,测试其它机器,则构建一个InetAddress对象
			try {
				
				DatagramSocket s = new DatagramSocket(new InetSocketAddress(ip, MINPORT));
				s.close();
				s = null;
				return MINPORT;
			} catch (BindException e) {
				// 如果报错就说明报错了,继续测试上面的.
				continue;
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
	
		}
	
		// 如果都在用就返回-1
		return -1;
	}
	
	public DatagramChannel getFreeChannel(String ip){
		DatagramChannel ch = null;
		int MINPORT = 2048;
		int MAXPORT = 65000;
		
		try {
			ch = DatagramChannel.open();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		for (; MINPORT < MAXPORT; MINPORT+=8) {
			// 第二个为测试本机IP,测试其它机器,则构建一个InetAddress对象
			try {
				
				ch.bind(new InetSocketAddress(ip, MINPORT));
				return ch;
			} catch (BindException e) {
				// 如果报错就说明报错了,继续测试上面的.
				continue;
			} catch (SocketException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
		}
		return ch.isConnected()?ch:null;
	}
	
	public InetAddress getInet4AddressByHardwareAddress(String hardware_address){
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()){
				NetworkInterface netInterface = interfaces.nextElement();
				if(netInterface.getHardwareAddress() != null)
				if(Packer.fromBytesTo16radix(netInterface.getHardwareAddress()).equalsIgnoreCase(hardware_address)){
					Enumeration<InetAddress> inets = netInterface.getInetAddresses();
					while(inets.hasMoreElements()) {
						InetAddress addr = inets.nextElement();
						if(addr instanceof Inet4Address){
							return addr;
						}
					}
					return null;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void finalize(){
		if(datagramChannel != null && datagramChannel.isOpen())
		try {
			datagramChannel.disconnect();
			datagramChannel.socket().close();
			datagramChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		datagramChannel = null;
		if(selector != null && selector.isOpen())
		try {
			selector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		selector = null;
	}
	
	@Override
	public String getLocalIP() {
		try {
			return ((InetSocketAddress)datagramChannel.getLocalAddress()).getAddress().getHostAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int getLocalPort() {
		try {
			return ((InetSocketAddress)datagramChannel.getLocalAddress()).getPort();
		} catch (IOException e) {
			e.printStackTrace();
		};
		return -1;
	}
}
