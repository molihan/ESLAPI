package com.sio.alg;

import com.sio.model.DataReader;

public interface TagInfo {
	public void getTags(String ip, String port, byte[] data, DataReader reader);
	
}
