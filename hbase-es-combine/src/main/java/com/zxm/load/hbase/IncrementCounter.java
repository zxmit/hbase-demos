package com.zxm.load.hbase;

public class IncrementCounter {

	private long count;
	
	public synchronized long addAndGet(long count) {
		this.count += count;
		return this.count;
	}
	
	public synchronized long reduceAndGet(long count) {
		this.count -= count;
		return this.count;
	}
	
	public long get() {
		return this.count;
	}
}
