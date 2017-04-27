package lam.design.pattern.chainofresponsibility;

import lam.log.Console;

/**
* <p>
* 模拟击鼓传花游戏
* </p>
* @author linanmiao
* @date 2017年4月27日
* @version 1.0
*/
public abstract class Player {
	
	protected Player successor;
	
	public Player(){}
	
	protected void setSuccessor(Player successor){
		this.successor = successor;
	}
	
	public void next(int index){
		if(this.successor != null){
			this.successor.handle(index);
		}else{
			Console.println("Game terminated.");
		}
	}
	
	public abstract void handle(int index);
	
	public static enum Order{
		JIAMU(1),
		JIASHE(2),
		JIAZHENG(3),
		JIABAOYU(4),
		JIAHUAN(5);
		
		private int index;
		
		private Order(int index){
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
	}

}
