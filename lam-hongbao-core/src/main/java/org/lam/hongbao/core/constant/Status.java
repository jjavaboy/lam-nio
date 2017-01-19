package org.lam.hongbao.core.constant;
/**
* <p>
* 
* </p>
* @author linanmiao
* @date 2017年1月19日
* @version 1.0
*/
public interface Status {
	
	enum HongBao{
		
	}
	
	enum HongBaoRecord{
		UNCONSUME((byte)0, "未消费"),
		CONSUME((byte)1, "已消费");
		private byte value;
		private String describe;
		
		HongBaoRecord(byte value, String describe){
			this.value = value;
			this.describe = describe;
		}
		
		public byte getValue() {
			return value;
		}
		
		public String getDescribe() {
			return describe;
		}
	}

}
