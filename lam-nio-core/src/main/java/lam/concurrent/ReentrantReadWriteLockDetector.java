package lam.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月18日
* @versio 1.0
*/
public class ReentrantReadWriteLockDetector {
	
	public static void main(String[] args) {
		ReentrantReadWriteLock readWriteLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
		
		ReadLock readLock = readWriteLock.readLock();
		WriteLock writeLock = readWriteLock.writeLock();
		
		readLock.lock();
		try {
			
		} finally {
			readLock.unlock();
		}
		
		writeLock.lock();
		try {
			
		} finally {
			writeLock.unlock();
		}
		
	}

}
