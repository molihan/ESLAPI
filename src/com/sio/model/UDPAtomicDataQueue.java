package com.sio.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现了DataQueue接口，对UDP即将发送的数据进行封装。
 * @author S
 *
 */
public class UDPAtomicDataQueue implements DataQueue {

	private List<AtomicData> data = new ArrayList<>();
	
	@Override
	public void putData(AtomicData atomicData) {
		data.add(atomicData);
	}

	@Override
	public AtomicData drillAtomicData() {
		AtomicData atomicData = data.get(0);
		data.remove(0);
		return atomicData;
	}

	@Override
	public boolean hasRemaining() {
		return !data.isEmpty();
	}

}
