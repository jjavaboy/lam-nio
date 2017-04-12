import java.util.Random;

import lam.delaytask.DelaySegment;
import lam.delaytask.DelayTaskRunner;
import lam.delaytask.support.Segment;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月12日
* @version 1.0
*/
public class DelayTaskTest {
	
	public static void main(String[] args){
		DelayTaskRunner runner = new DelayTaskRunner();
		runner.start();
		
		sleep(20);
		
		Random r = new Random();
		for(int i = 0; i < 100; i++){
			sleep(100);
			Segment.Task task = new DelaySegment.DelayTask(i, r.nextInt(2), r.nextInt(3600));
			runner.addTask(task);
		}
	}
	
	private static void sleep(long millisecond){
		try {
			Thread.sleep(millisecond);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
