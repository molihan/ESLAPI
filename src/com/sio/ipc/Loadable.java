package com.sio.ipc;

import java.util.Map;

import com.sio.plugin.Terminal;

public interface Loadable {
	
	public Terminal getUnresolvedTerminal();
	public Map<String,Terminal> getLoadedTerminals();
	public void reloadPlugins();
	public void clear();
	
}
