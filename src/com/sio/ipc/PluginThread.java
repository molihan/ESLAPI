package com.sio.ipc;

public interface PluginThread {
	/**
	 * Before run, the clients need to initialize first.
	 */
	public void loadAndInit();
	/**
	 * Start to rend the plugin's thread.
	 */
	public void go();
	/**
	 * Stop to rend the plugin's thread.
	 */
	public void stop();
	/**
	 * Reset and re-start to rend the plugin's thread.
	 */
	public void reset();
}
