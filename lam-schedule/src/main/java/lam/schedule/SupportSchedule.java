package lam.schedule;

import java.util.Date;

/**
* <p>
* base schedule class
* </p>
* @author linanmiao
* @date 2017年5月9日
* @version 1.0
*/
public interface SupportSchedule extends Runnable{

	public void run();
	
	public void cancelNext(Date fromDate, Date toDate);
}
