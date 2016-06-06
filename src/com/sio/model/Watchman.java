package com.sio.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Watchman implements Watcher {
	private static final int OFFLINE_TAG_TIMEOUT = 120;
	private static final int OFFLINE_AP_TIMEOUT = 20;
	private static final int MILLIS_IN_SECOND = 1000;
	private static Map<String, Long> prisoners = new HashMap<>();
	
//	instance
	private static final Watcher instance = new Watchman();
	
	private Watchman() {
	}

	@Override
	public void run() {
		clear();
	}

	@Override
	public void sign(String prisoner) {
		prisoners.put(prisoner, System.currentTimeMillis());
	}

	@Override
	public void clear() {
		Set<AbstractAccessPoint> aps = new HashSet<>();
		aps.addAll(UtilityProvider.getUtility().getAccessPoints());
		for(AbstractAccessPoint ap : aps){
			String ap_ip = ap.getIp();
			if(System.currentTimeMillis() - prisoners.get(ap_ip) > OFFLINE_AP_TIMEOUT*MILLIS_IN_SECOND){
				ap.setAlive(false);
			}
			
			Collection<WirelessTag> wTags = new HashSet<>();
			wTags.addAll(ap.getTags());
			for(WirelessTag wTag : wTags){
				if(System.currentTimeMillis() - wTag.last_feedback >= OFFLINE_TAG_TIMEOUT*MILLIS_IN_SECOND){
					wTag.online(false);
				}
			}
		}
	}

	public static Watcher getInstance(){
		return instance;
	}
}
