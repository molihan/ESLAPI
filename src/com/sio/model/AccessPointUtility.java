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
				interpreter.getPrototype();
			}
			
		}
	}
	
}
