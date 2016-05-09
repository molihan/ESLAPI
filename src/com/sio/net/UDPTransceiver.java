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
	 */
	public void startUDPEvent();
	/**
	 * Stop the UDP auto-write and auto-read event
	 */
	public void stopUDPEvent();
}
