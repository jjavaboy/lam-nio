package lam.schedule.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lam.schedule.service.ExternalService;
import lam.util.DateUtil;
import lam.weixin.core.model.BehaveTrace;

/**
* <p>
* 提供接口给外部使用，比如微信端
* </p>
* @author linanmiao
* @date 2017年5月9日
* @version 1.0
*/
@Service(value="externalService")
public class ExternalServiceImpl implements ExternalService {
	
	@Resource(name="lamMailScheduleProxy")
	private lam.schedule.SupportSchedule supportSchedule;

	@Override
	public boolean weixinCheckin(List<BehaveTrace> list) {
		if(list == null || list.isEmpty()){
			return false;
		}
		for(BehaveTrace behaveTrace : list){
			if((behaveTrace.getCode() != null && behaveTrace.getCode() == 1) || "SLEE".equals(behaveTrace.getKey())){
				Date now = new Date();
				Calendar calendar = DateUtil.addToCalendar(now, Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				now = calendar.getTime();
				Date fromDate = DateUtil.add(now, Calendar.MINUTE, -5);
				Date toDate = DateUtil.add(now, Calendar.MINUTE, 5);
				supportSchedule.cancelNext(fromDate, toDate);
			}
		}
		return true;
	}

}
