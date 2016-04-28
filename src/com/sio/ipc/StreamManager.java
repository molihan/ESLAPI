package com.sio.ipc;

import java.io.Reader;
import java.io.Writer;

public class StreamManager implements StreamDistributable {

	private Writer outputStream;
	private Reader inputStream;
	
	public StreamManager() {
		outputStream = System.console().writer();
		inputStream = System.console().reader();
	}

	@Override
	public Writer getWriter() {
		return outputStream;
	}

	@Override
	public Reader getReader() {
		return inputStream;
	}

}
