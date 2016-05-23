package com.sio.object;

import com.sio.ipc.ConsoleSystem;
import com.sio.ipc.DefaultConsole;
import com.sio.ipc.PluginGo;
import com.sio.ipc.PluginThread;
import com.sio.model.AccessPointUtility;
import com.sio.model.DefaultCastSettingSelector;
import com.sio.model.DefaultDimensionSelector;
import com.sio.model.UtilityProvider;
import com.sio.net.UDPTransceiver;
import com.sio.util.DefaultUDPTransceiverFactory;
import com.sio.util.ImageCasterDelegatesFactory;
import com.sio.util.UDPConnectionFactory;

public class APIService {
	private static final String VERSION = "2.0.1a";
	private static final String RELEASE_DATE = "2016-03-30";
	
	public static void main(String[] args) {

		new APIService();
	}
	
	public APIService() {
		{	
			//create a concrete model
			UtilityProvider.getInstance().initUtility(new AccessPointUtility());
			UDPConnectionFactory factory = new DefaultUDPTransceiverFactory();
			UDPTransceiver transceiver = factory.createUDPTransceiver();
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
			
			System.out.println("cuurent version: " + VERSION);
			System.out.println("released date: " + RELEASE_DATE);
//			commandLine.startGatherCommand();
			
			//UDP Ongoing
			transceiver.startUDPEvent(false);
		}
	}
}
