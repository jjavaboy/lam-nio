package org.lam.hongbao.core.service;

import org.lam.hongbao.core.model.HongBao;

/**
* <p>
* hong bao behave
* </p>
* @author linanmiao
* @date 2017年1月18日
* @versio 1.0
*/
public interface HongBaoBehave {
	
	/**
	 * 发布红包
	 * @param hongBao
	 * @return
	 */
	public boolean issueHongBao(HongBao hongBao);
	
	/**
	 * 抢红包
	 * @param userId
	 * @param hongbaoId
	 * @return
	 */
	public boolean takeHongBao(long userId, long hongbaoId);
	
	/**
	 * 将抢的红包记录更新到库，如果红包已经抢完，则更新红包状态
	 * @return
	 */
	public boolean finalizeHongBao(long hongbaoId);

}
