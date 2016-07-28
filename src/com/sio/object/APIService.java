package com.sio.object;

import org.apache.log4j.Logger;

import com.sio.ipc.ConsoleSystem;
import com.sio.ipc.DefaultConsole;
import com.sio.ipc.PluginGo;
import com.sio.ipc.PluginThread;
import com.sio.model.AccessPointUtility;
import com.sio.model.DefaultCastSettingSelector;
import com.sio.model.DefaultDimensionSelector;
import com.sio.model.net.UDPTransceiver;
import com.sio.util.DefaultUDPTransceiverFactory;
import com.sio.util.ImageCasterDelegatesFactory;
import com.sio.util.UDPConnectionFactory;

public class APIService {
	private static final String VERSION = "2.0.3a";
	private static final String RELEASE_DATE = "2016-06-06";
	
//	logger
	private static final Logger logger = Logger.getLogger(APIService.class);
	
	public static void main(String[] args) {
		new APIService();
	}
	
	public APIService() {
		try{
			launchService();
		} catch (Exception e){
			logger.error(e.getMessage());
		}
	}
	
	private void launchService(){
		{	
			//create a concrete model
			APIServiceManager.setDevices(AccessPointUtility.instance);
			UDPConnectionFactory factory = new DefaultUDPTransceiverFactory();
			UDPTransceiver transceiver = factory.createUDPTransceiver();
			APIServiceManager.setTransceiver(transceiver);
			ImageCasterDelegatesFactory.setCastSettingSelector(new DefaultCastSettingSelector());
			ImageCasterDelegatesFactory.setDimensionSelector(new DefaultDimensionSelector());
			
			//Console
			ConsoleSystem commandLine = new DefaultConsole();
			
			//Client Ongoing
			PluginThread plug = new PluginGo();
			plug.loadAndInit();
			plug.go();
			System.out.println("plugins loaded...");
			
			//Destroy hook
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					plug.stop();
					commandLine.stopGatherCommand();
					transceiver.stopUDPEvent();
					System.out.println("safety shutdown.");
				}
			}));
			//Info
			System.out.println("console is running...");
			
			System.out.println("cuurent core version: " + VERSION);
			System.out.println("released date: " + RELEASE_DATE);
//			commandLine.startGatherCommand();
			
			//UDP Ongoing & asynchronize mode
			transceiver.startUDPEvent(false);
		}
	}
}
