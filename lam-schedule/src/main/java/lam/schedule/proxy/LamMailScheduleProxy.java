package lam.schedule.proxy;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.log.LogSupport;
import lam.schedule.SupportSchedule;
import lam.util.DateUtil;

/**
* <p>
* schedule proxy
* </p>
* @author linanmiao
* @date 2017年5月9日
* @version 1.0
*/
public class LamMailScheduleProxy extends LogSupport implements SupportSchedule{
	
	private static Logger logger = LoggerFactory.getLogger(LamMailScheduleProxy.class);
	
	private volatile Date fromDate;
	
	private volatile Date toDate;
	
	private SupportSchedule supportSchedule;
	
	public LamMailScheduleProxy(SupportSchedule supportSchedule){
		this.supportSchedule = supportSchedule;
	}

	@Override
	public void run() {
		if(skipSchedule()){
			return ;
		}
		reloadSkip();
		super.start();
		supportSchedule.run();
		long timeCost = super.endAndCost();
		logger.info("run, param:{} ==>> result:{}, timeCost(ms):{}", null, null, timeCost);
	}
	
	private boolean skipSchedule(){
		final Date fDate = this.fromDate;
		final Date tDate = this.toDate;
		if(fDate == null || tDate == null){
			return false;
		}
		Date now = new Date();
		if(fDate.compareTo(now) < 0 && tDate.compareTo(now) > 0){
			logger.info("skipSchedule, fromDate:{}, now:{}, toDate:{}", DateUtil.getCurrentTime(fDate, "yyyy-MM-dd HH:mm:ss"), 
					DateUtil.getCurrentTime(now, "yyyy-MM-dd HH:mm:ss"), DateUtil.getCurrentTime(tDate, "yyyy-MM-dd HH:mm:ss"));
			return true;
		}
		return false;
	}
	
	private void reloadSkip(){
		this.fromDate = null;
		this.toDate = null;
	}

	@Override
	public void cancelNext(Date fromDate, Date toDate) {
		this.fromDate = fromDate;
		this.toDate = toDate;
		logger.info("cancelNext, param:fromDate:{}, toDate:{}", 
				DateUtil.getCurrentTime(fromDate, "yyyy-MM-dd HH:mm:ss"), DateUtil.getCurrentTime(toDate, "yyyy-MM-dd HH:mm:ss"));
	}

}
