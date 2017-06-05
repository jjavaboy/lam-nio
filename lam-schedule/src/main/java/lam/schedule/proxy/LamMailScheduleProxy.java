package lam.schedule.proxy;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.log.LogSupport;
import lam.schedule.SupportSchedule;
import lam.schedule.constant.Constant;
import lam.schedule.container.ContainerManager;
import lam.schedule.container.FilecacheContainer;
import lam.schedule.util.SystemProperties;
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
	
	private volatile int cancelScheduleInAdvanceHour;
	
	private static final String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";
	
	private SupportSchedule supportSchedule;
	
	public LamMailScheduleProxy(SupportSchedule supportSchedule){
		this.supportSchedule = supportSchedule;
		this.cancelScheduleInAdvanceHour = SystemProperties.getPropertyInt(Constant.JVM_CM_SCHEDULE_IN_ADVANCE_HOUR);
		reloadSkip();
	}

	@Override
	public void run() {
		if(skipSchedule()){
			return ;
		}
		nullSkip();
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
			logger.info("skipSchedule, fromDate:{}, now:{}, toDate:{}", DateUtil.getCurrentTime(fDate, DATE_FORMATE), 
					DateUtil.getCurrentTime(now, DATE_FORMATE), DateUtil.getCurrentTime(tDate, DATE_FORMATE));
			return true;
		}
		return false;
	}
	
	private void reloadSkip(){
		FilecacheContainer c = (FilecacheContainer) ContainerManager.getInstance().get(ContainerManager.Type.FILECACHE);
		if(!c.contains("fromDate") || !c.contains("toDate")){
			return ;
		}
		String fDate = c.get("fromDate");
		String tDate = c.get("toDate");
		if(StringUtils.isNotBlank(fDate) && StringUtils.isNotBlank(tDate)){
			this.fromDate = DateUtil.toDate(fDate, DATE_FORMATE);
			this.toDate = DateUtil.toDate(tDate, DATE_FORMATE);
		}
	}
	
	private void nullSkip(){
		this.fromDate = null;
		this.toDate = null;
		FilecacheContainer c = (FilecacheContainer) ContainerManager.getInstance().get(ContainerManager.Type.FILECACHE);
		if(c.contains("fromDate")){			
			c.remove("fromDate");
		}
		if(c.contains("toDate")){			
			c.remove("toDate");
		}
	}

	@Override
	public void cancelNext(Date fromDate, Date toDate) {
		if(fromDate.compareTo(toDate) > 0){
			throw new IllegalArgumentException(String.format("fromDate(%s) greater than toDate(%s)", fromDate, toDate));
		}
		Date now = new Date();
		if(now.compareTo(toDate) > 0){
			throw new IllegalArgumentException(String.format("now(%s) greater than toDate(%s)", fromDate, toDate));
		}
		StringBuilder logBuilder = new StringBuilder();
		logBuilder.append(String.format("cancelNext, param:fromDate:%s, toDate:%s", 
				DateUtil.getCurrentTime(fromDate, "yyyy-MM-dd HH:mm:ss"), DateUtil.getCurrentTime(toDate, "yyyy-MM-dd HH:mm:ss")));
		int diffHour = (int) ((fromDate.getTime() - now.getTime()) / (60 * 60 * 1000));
		if(diffHour > this.cancelScheduleInAdvanceHour){
			nullSkip();
			String s = ", cancel time is in advance " + diffHour + " hour, we won't cancel the schedule, the schedule will be performed as usual.";
			logBuilder.append(s);
			logger.info(logBuilder.toString());
			return ;
		}
		logger.info(logBuilder.toString());
		this.fromDate = fromDate;
		this.toDate = toDate;
		FilecacheContainer c = (FilecacheContainer) ContainerManager.getInstance().get(ContainerManager.Type.FILECACHE);
		c.set("fromDate", DateUtil.getCurrentTime(fromDate, DATE_FORMATE));
		c.set("toDate", DateUtil.getCurrentTime(toDate, DATE_FORMATE));
		supportSchedule.cancelNext(fromDate, toDate);
	}

}
