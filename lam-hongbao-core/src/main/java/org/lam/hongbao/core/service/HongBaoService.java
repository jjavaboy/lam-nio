package org.lam.hongbao.core.service;

import org.lam.hongbao.core.model.HongBao;

/**
* <p>
* hong bao service
* </p>
* @author linanmiao
* @date 2017年1月18日
* @versio 1.0
*/
public interface HongBaoService {
	
	public boolean saveHongBao(HongBao hongBao);
	
	public boolean updateHongBao(HongBao hongBao);
	
	public HongBao findHongBao(long id);

}
