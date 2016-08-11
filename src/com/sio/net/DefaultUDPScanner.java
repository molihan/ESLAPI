package com.sio.net;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sio.model.DefaultPackSheer;
import com.sio.model.DefaultUDPA5Pack;
import com.sio.model.net.UDPScanner;

public class DefaultUDPScanner implements UDPScanner {
	private List<String> targetIP;
	private static String usableIP;
	private byte[] publish;
	private boolean runloop = true;
	private int port;
	@Override
	public String scanUsableUDP(int port) {
		this.port = port;
		buzz();
		return usableIP;
	}

	public static String getUsableIP() {
		return usableIP;
	}

	public static void setUsableIP(String usableIP) {
		DefaultUDPScanner.usableIP = usableIP;
	}

	private byte[] initPublishData() {
		DefaultUDPA5Pack a5_pack = new DefaultUDPA5Pack();
		a5_pack.generatePack(port);
		byte[] pack = a5_pack.getData();
		DefaultPackSheer sheer = new DefaultPackSheer();
		sheer.putData(DefaultUDPA5Pack.COMM_FLAG,pack);
		pack = sheer.getPack();
		return pack;
	}

	private void buzz(){
		while (runloop) {
			final UDPBuzzer udp = new UDPBuzzer();
			publish = initPublishData();
			udp.setPoster(publish);
			getAllIP();
			for (String ip : targetIP) {
				String targetIP = initPublishAddress(ip);
				udp.sendPoster(ip, port+1, targetIP, port);
			}
			
			Thread[] threads = new Thread[targetIP.size()];
			for(int x=0; x<targetIP.size(); x++){
				final int i = x;
				threads[i] = new Thread(()->{
					String echo = udp.echo(targetIP.get(i), port+1);
					if(echo != null){
						usableIP = echo;
					}
				});
				threads[i].start();
			}
			for(Thread thread : threads){
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(usableIP != null && usableIP.length() > 0){
				System.out.println("INFO:[UDPScanner] " + usableIP + " -> DETECTED.");
				runloop = false;
			}
		}
	}

	private String initPublishAddress(String ip) {

		String cast_ip = ip;
		Matcher mc = Pattern.compile(
				"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}").matcher(cast_ip);
		if (mc.find()) {
			cast_ip = cast_ip.trim();
			int index = cast_ip.lastIndexOf(".");
			cast_ip = cast_ip.substring(0, index + 1);
			cast_ip = cast_ip.concat("255");
		}
		return cast_ip;
	}
	

	private void getAllIP() {
		targetIP = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		for (NetworkInterface netint : Collections.list(interfaces)) {

			try {
				if (null != netint.getHardwareAddress()) {
					List<InterfaceAddress> ips = netint.getInterfaceAddresses();
					for (InterfaceAddress address : ips) {
						String ip_temp = address.getAddress().getHostAddress();
						Matcher mc = Pattern.compile(
								"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")
								.matcher(ip_temp);
						if (mc.find()) {
							if (!ip_temp.startsWith("127.0")) {
								targetIP.add(ip_temp);
							}
						}

					}

				}
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}

	}
	
}
