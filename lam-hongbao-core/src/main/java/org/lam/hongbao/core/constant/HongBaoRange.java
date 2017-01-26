package org.lam.hongbao.core.constant;
/**
* <p>
* 红包范围
* </p>
* @author linanmiao
* @date 2017年1月19日
* @version 1.0
*/
public enum HongBaoRange {
	
	MIN(0.01),
	
	MAX(200);
	
	private double value;
	
	private HongBaoRange(double value){
		this.value = value;
	}
	
	public double getValue(){
		return this.value;
	}

}
