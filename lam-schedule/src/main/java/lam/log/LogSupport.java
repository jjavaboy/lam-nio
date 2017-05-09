package lam.log;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年5月9日
* @version 1.0
*/
public class LogSupport {
	
	private long time;
	
	public long start(){
		this.time = now();
		return this.time;
	}
	
	public long endAndCost(){
		long end = now();
		long tempTime = this.time;
		this.time = end;
		return end - tempTime;
	}
	
	private long now(){
		return System.currentTimeMillis();
	}
}
