package com.sio.net.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sio.model.DataQueue;
import com.sio.model.DataReader;
import com.sio.model.Interpreter;
import com.sio.model.UDPAtomicDataQueue;
import com.sio.model.UDPInterpreter;
import com.sio.net.DefaultUDPTransceiver;

public class TestDefaultUDPTransceiver {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUDP() {
		DefaultUDPTransceiver transceiver = new DefaultUDPTransceiver();	//create transceiver
		DataReader observable = new DataReader();							//create reader
		Interpreter interpreter = new UDPInterpreter();						//create interpreter
//		observable.addObserver(null);
		observable.setInterpreter(interpreter);								//set interpreter to reader
		transceiver.setObservable(observable);								//set reader to transceiver
		DataQueue queue = new UDPAtomicDataQueue();							//create queue
		transceiver.setQueue(queue);										//set queue
		transceiver.startUDPEvent();
		while(true);
	}

	@Test
	public void testUDP1() throws IOException {
		Selector sel = java.nio.channels.Selector.open();
		DatagramChannel channel = DatagramChannel.open();
		channel.bind(new InetSocketAddress(1920));
		channel.configureBlocking(false);
		channel.register(sel, SelectionKey.OP_READ);
		for(int x=0; x<10; x++){
			sel.select();
			Set<SelectionKey> keys = sel.selectedKeys();
			for(SelectionKey key : keys){
				if(key.isReadable()){
					System.out.println("read");
					ByteBuffer buff = ByteBuffer.allocate(20);
					channel.read(buff);
				}
				System.out.println("once");
			}
		}
		
	}
}
