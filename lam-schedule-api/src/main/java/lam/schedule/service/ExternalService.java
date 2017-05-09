package lam.schedule.service;

import java.util.List;

import lam.weixin.core.model.BehaveTrace;

/**
* <p>
* external service
* </p>
* @author linanmiao
* @date 2017年5月9日
* @version 1.0
*/
public interface ExternalService {
	
	public boolean weixinCheckin(List<BehaveTrace> list);

}
