package com.sio.model;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
/**
 * Collection class
 * 
 * @author S
 *
 */
public final class AccessPointUtility implements DeviceUtility{

	private static Set<AbstractAccessPoint> accesspoints;
	public AccessPointUtility() {
		accesspoints = new HashSet<AbstractAccessPoint>();
	}

	public Set<AbstractAccessPoint> getAccessPoints(){
		return accesspoints;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof DataReader){
			String ip = ((DataReader) o).getSrc_ip();
			int port = ((DataReader) o).getSrc_port();
			Interpreter interpreter = ((DataReader) o).getInterpreter();
			int type = interpreter.getType();
			if(type == Interpreter.TYPE_TAG){
				AbstractAccessPoint ap = new DefaultAccessPoint();
				ap.setIp(ip);
				ap.setPort(port);
				if(!accesspoints.contains(ap)){
					accesspoints.add(ap);
				}
				for(AbstractAccessPoint accesspoint : accesspoints){
					if(accesspoint.equals(ap)){
						WirelessTag tag = new DefaultUDPTag();
						InterpretedTag iTag = null;
						if(arg instanceof InterpretedTag){
							iTag = (InterpretedTag) arg;
						} else {
							System.out.println(arg);
						}
						tag.setMac(iTag.mac());
						tag.setTag(iTag);
						if(accesspoint.contains(tag)){
							accesspoint.getTag(iTag.mac()).setTag(iTag);	//refresh data
						} else {
							accesspoint.addTag(tag);
						}
						
					}
				}
			}
			
		}
	}
	
}
