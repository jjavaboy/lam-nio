package lam.design.pattern.chainofresponsibility.rh;

import com.google.gson.Gson;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月27日
* @versio 1.0
*/
public abstract class AbstractRequest {
	
	protected Level level;
	
	public AbstractRequest(Level level){
		this.level = level;
	}
	
	public Level getLevel(){
		return this.level;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	public static enum Level{
		
		L01(1),
		L02(2),
		L03(3);
		
		int value;
		
		private Level(int value){
			this.value = value;
		}
	}

}
