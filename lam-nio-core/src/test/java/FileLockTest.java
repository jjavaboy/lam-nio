import java.io.File;

import lam.concurrent.lock.LFileLock;
import lam.concurrent.lock.support.FileOperate;

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
		final File file = new File("C:\\Users\\Sky lin\\dubbo\\cache\\lam-dubbo-provider.cache.lock0");
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
									e.printStackTrace();
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
