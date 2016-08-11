package com.sio.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

public class UDPBuzzer {
//	private DatagramSocket server;

	private byte[] publish;

	static Logger logger = Logger.getLogger(UDPBuzzer.class);

	public UDPBuzzer() {

	}

	public void setPoster(byte[] poster) {
		publish = poster;

	}

	public void sendPoster(String ip, int port, String targetIP, int targetPort) {
		DatagramPacket pack;
		try (DatagramSocket server = new DatagramSocket(new InetSocketAddress(
				ip, port))) {
			pack = new DatagramPacket(publish, publish.length,
					new InetSocketAddress(targetIP, targetPort));
			server.send(pack);
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("Route lost 1");
			System.exit(0);
		}

	}

	public String echo(String ip, int port) {
		byte[] sample = new byte[14];
		DatagramPacket datagramPacket = new DatagramPacket(sample, 14);
		String echoString = null;
		try (DatagramSocket server = new DatagramSocket(new InetSocketAddress(
				ip, port))) {
			server.setSoTimeout(3000);
			server.receive(datagramPacket);
			String hex = Integer.toHexString(sample[0] & 0xFF);
			if(hex.equalsIgnoreCase("a4")){
				echoString = ip;
				server.disconnect();
				server.close();
			}
		} catch (SocketTimeoutException e){
			System.out.println("INFO:[UDPScanner] " + ip + " -> CHECKED.");
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("Route lost 2");
			System.exit(0);
		} 
		return echoString;
	}
}
