package com.sio.model;


public interface DataQueue {

	public void putData(AtomicData atomicData);
	public AtomicData drillAtomicData();
	public boolean hasRemaining();
}
 