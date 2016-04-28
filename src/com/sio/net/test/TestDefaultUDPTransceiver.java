package com.sio.net.test;

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

}
