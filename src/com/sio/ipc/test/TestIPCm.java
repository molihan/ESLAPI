package com.sio.ipc.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sio.ipc.IPCm;
import com.sio.ipc.PluginGo;

public class TestIPCm {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadClass(){
		{
			IPCm ipc = IPCm.instance;
			ipc.reloadPlugins();
		
		}
	}
	
	@Test
	public void testPlugGo(){
		{
			PluginGo plug = new PluginGo();
			plug.go();	
		}
	}

}
