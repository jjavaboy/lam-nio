package lam.design.pattern.chainofresponsibility;

import lam.log.Console;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月27日
* @version 1.0
*/
public class DrumBeater {
	
	public static void main(String[] args){
		int index = 5;
		if(args != null && args.length > 0){
			try{
				index = Integer.parseInt(args[0]);
			}catch(NumberFormatException n){
				Console.error(n);
			}
		}
		Player player = new JiaMu(new JiaShe(new JiaZheng(new JiaBaoYu(new JiaHuan(null)))));
		player.handle(index);
	}

}
