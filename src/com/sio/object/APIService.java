package com.sio.object;

import org.apache.log4j.Logger;

import com.sio.ipc.ConsoleSystem;
import com.sio.ipc.DefaultConsole;
import com.sio.ipc.PluginGo;
import com.sio.ipc.PluginThread;
import com.sio.model.AccessPointUtility;
import com.sio.model.DefaultCastSettingSelector;
import com.sio.model.DefaultDimensionSelector;
import com.sio.model.net.UDPScanner;
import com.sio.model.net.UDPTransceiver;
import com.sio.net.DefaultUDPScanner;
import com.sio.net.DefaultUDPTransceiver;
import com.sio.util.DefaultUDPTransceiverFactory;
import com.sio.util.ImageCasterDelegatesFactory;
import com.sio.util.NetFileConfigure;
import com.sio.util.UDPConnectionFactory;

/**
 * The API </br>
 * Call the construction function to holding the service.</br>
 * 
 * @author S
 *
 */
public class APIService {
	private static final String VERSION = "2.0.5a";
	private static final String RELEASE_DATE = "2016-07-28";
	
//	logger
	private static final Logger logger = Logger.getLogger(APIService.class);
	
	public static void main(String[] args) {
		new APIService();
	}
	
	/**
	 * This function will trigger a service start up.<br>
	 * The object provides a service manager that could associated with this API.
	 */
	public APIService() {
		try{
			launchService();
		} catch (Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	/*
	 * The construction function will call this function.
	 */
	private void launchService(){
		{	
			
			//create a concrete model
			APIServiceManager.setDevices(AccessPointUtility.instance);
			UDPConnectionFactory factory = new DefaultUDPTransceiverFactory();
			//Test network 
			UDPScanner scanner = new DefaultUDPScanner();
			String reconIP = scanner.scanUsableUDP(DefaultUDPTransceiver._COM_PORT_);
			new NetFileConfigure().setIP(reconIP);
			//...
			UDPTransceiver transceiver = factory.createUDPTransceiver(NetFileConfigure.getIP());
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
	
	/**
	 * Return an APIServiceManager object that inherited Utility set and transceiver set.
	 * @return APIServiceManager.
	 */
	public APIServiceManager getManager(){
		return APIServiceManager.getInstance();
	}
}
