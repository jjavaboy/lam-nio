import java.io.File;

import lam.concurrent.lock.LFileLock;
import lam.concurrent.lock.support.FileOperate;
import lam.log.Console;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月27日
* @version 1.0
*/
public class FileLockTest {

	public static void main(String[] args) {
		final File file = new File("...");
		for(int i = 0; i < 3; i++){
			final int j = i;
			new Thread(){
				public void run(){
					boolean result = 	
					new LFileLock(file, FileOperate.RW){
							@Override
							protected boolean execute() {
								try {
									Thread.sleep(1000L);
								} catch (InterruptedException e) {
									Console.println(getName() + " thread has been innterrupted.");
									e.printStackTrace();
									return false;
								}
								return true;
							}
						}.lockAndRun();
					System.out.println("i:" + j + ", result:" + result);
				}
			}.start();
		}
	}

}
