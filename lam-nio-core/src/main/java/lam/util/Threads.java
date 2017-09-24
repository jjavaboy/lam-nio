package lam.util;

import java.util.concurrent.TimeUnit;

/**
* <p>
* Thread util class
* </p>
* @author linanmiao
* @date 2017年9月6日
* @version 1.0
*/
public class Threads {
	
	public static void sleepWithUninterrupt(long millisecond){
		boolean isInterrupted = false;
		long start = System.currentTimeMillis();
		long remainSleepTime = millisecond;
		try{
			while(true){
				try {
					//If remainSleepTime less than or equal to zero, do not sleep at all.
					//do not use Thread.sleep(millisecond)
					TimeUnit.MILLISECONDS.sleep(remainSleepTime);
					return ;
				} catch (InterruptedException e) {
					e.printStackTrace();
					isInterrupted = true;
					remainSleepTime = System.currentTimeMillis() - start;
				}
			}
		}finally{
			if(isInterrupted){
				Thread.currentThread().interrupt();
			}
		}
	}

}
