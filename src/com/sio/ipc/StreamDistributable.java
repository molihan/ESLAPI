package com.sio.ipc;

import java.io.Reader;
import java.io.Writer;

public interface StreamDistributable {

	public Writer getWriter();
	public Reader getReader();
}
