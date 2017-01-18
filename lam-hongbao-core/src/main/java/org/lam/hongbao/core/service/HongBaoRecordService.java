package org.lam.hongbao.core.service;

import org.lam.hongbao.core.model.HongBaoRecord;

/**
* <p>
* hong bao service
* </p>
* @author linanmiao
* @date 2017年1月18日
* @versio 1.0
*/
public interface HongBaoRecordService {
	
	public boolean saveHongBao(HongBaoRecord hongBao);
	
	public boolean updateHongBao(HongBaoRecord hongBao);
	
	public HongBaoRecord findHongBao(long id);

}
