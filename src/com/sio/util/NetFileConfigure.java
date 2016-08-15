package com.sio.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class NetFileConfigure {
	private static final File PROPS_FILE = new File("./config/ip.ini");
	private static final String PROP_FILE_COMMENT =		"#DO NOT MENUALLY FIX THIS FILE. IT'S GENERATED EVERYTIME API START UP.\r\n";
	private static final String PROP_FILE_CONTENT = 	PROP_FILE_COMMENT + "ip=auto\r\n";
	public static final Properties PROPS = new Properties();
	public static final String KEY_IP = "ip";
	
	public NetFileConfigure() {
		if(PROPS_FILE.exists()){
			try {
				PROPS.load(new FileReader(PROPS_FILE));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if(!PROPS_FILE.getParentFile().exists()){
				PROPS_FILE.getParentFile().mkdirs();
			}
			try {
				PROPS_FILE.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try(FileWriter writer = new FileWriter(PROPS_FILE)){
				writer.write(PROP_FILE_CONTENT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				PROPS.load(new FileReader(PROPS_FILE));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getIP(){
		String mac = null;
		mac = PROPS.getProperty(KEY_IP);
		return mac;
	}
	
	public void setIP(String ip){
		PROPS.setProperty(KEY_IP, ip);
		try(FileWriter writer = new FileWriter(PROPS_FILE)){
			PROPS.store(writer, PROP_FILE_COMMENT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
