import java.util.Random;

import lam.concurrent.ExpiredConcurrentMap;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月15日
* @version 1.0
*/
public class ExpiredCucurrentMapTest {

	public static void main(String[] args) {
		ExpiredConcurrentMap<String, String> expiredMap = new ExpiredConcurrentMap<>();
		Random r = new Random(); 
		for(int i = 0; i < 1000; i++){
			expiredMap.set("key" + i, "value" + i, 10 * 000 + r.nextInt(6 * 100 * 1000));
		}
		
		synchronized (ExpiredCucurrentMapTest.class) {
			try {
				ExpiredCucurrentMapTest.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		expiredMap.close();
	}

}
