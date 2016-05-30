package com.sio.net;

import java.io.IOException;

public interface UDPTransceiver {
	/**
	 * Write data thru UDP protocol. Target at given ip and port.
	 * @param ip	target'IP
	 * @param port	target's port
	 * @param data	packet of data
	 * @throws IOException	may occurs if the channel is not initial yet.
	 */
	public void write(String ip, int port, byte[] data) throws IOException;
	/**
	 * Broadcast data thru UDP protocol. Target at given port.
	 * @param port	target's port
	 * @param data	broad data.
	 * @throws IOException may occurs if the channel is not initial yet.
	 */
	public void broadCast(int port, byte[] data) throws IOException;
	
	/**
	 * This function will start a new thread that holding ongoing events in a roll poling.
	 * @param synchronize If its true this thread will auto join the the caller's thread. If false, The function will not block.
	 * @return The process thread this function made.
	 */
	public Thread startUDPEvent(boolean synchronize);
	/**
	 * Stop the UDP auto-write and auto-read event
	 */
	public void stopUDPEvent();
	
	public String getLocalIP();
	
	public int getLocalPort();
}
