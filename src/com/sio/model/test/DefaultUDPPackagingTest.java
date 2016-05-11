package com.sio.model.test;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sio.model.DefaultUDPPackaging;

public class DefaultUDPPackagingTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetHead() {
		String mac = "A1B2C3D4E5F6";
		int random = 12345678;
		Date time = new Date(System.currentTimeMillis()); 
		DefaultUDPPackaging dup = new DefaultUDPPackaging();
		dup.setHead(mac, random, time);
		System.out.println(dup.fromBytesTo16radix(dup.getHead()));
	}

	@Test
	public void testSetData() {
		String mac = "A1B2C3D4E5F6";
		int random = 12345678;
		Date time = new Date(System.currentTimeMillis()); 
		byte[] data = new byte[]{0x05,0x05,0x05};
		byte order = 0x01;
		DefaultUDPPackaging dup = new DefaultUDPPackaging();
		dup.setHead(mac, random, time);
		for(int x=0; x<8; x++)	dup.setData(data, order);
		System.out.println(dup.fromBytesTo16radix(dup.getHead()));
		System.out.println(dup.fromBytesTo16radix(dup.getData()));
	}
}
