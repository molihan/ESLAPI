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
				WirelessTag tag = new DefaultUDPTag();
				InterpretedTag iTag = null;
				if(arg instanceof InterpretedTag){
					iTag = (InterpretedTag) arg;
					tag.setMac(iTag.mac());
				} else {
					try {
						throw new Exception("Error update pass in [com.sio.model.AccessPointUtility.update()]");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				for(AbstractAccessPoint accesspoint : accesspoints){
					if(accesspoint.equals(ap)){
						accesspoint.setAlive(true);
						if(accesspoint.contains(tag)){
							accesspoint.getTag(iTag.mac()).setTag(iTag);	//refresh data
							accesspoint.getTag(iTag.mac()).setLast_feedback(System.currentTimeMillis());
						} else {
							tag.setTag(iTag);
							tag.setLast_feedback(System.currentTimeMillis());
							accesspoint.addTag(tag);
						}
					} else {
						if(accesspoint.contains(tag)){
							accesspoint.removeTag(tag);
						}
					}
				}
			}
			
		}
	}
	
}
