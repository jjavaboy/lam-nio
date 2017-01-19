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
	
	MIN(1),
	
	MAX(200);
	
	private int value;
	
	private HongBaoRange(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}

}
