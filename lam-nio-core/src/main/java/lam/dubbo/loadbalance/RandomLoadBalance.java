package lam.dubbo.loadbalance;

import java.util.List;
import java.util.Random;

import lam.dubbo.loadbalance.support.Invoker;
import lam.dubbo.loadbalance.support.LoadBalance;

/**
* <p>
* 加权随机 负载均衡
* </p>
* @author linanmiao
* @date 2017年5月2日
* @version 1.0
*/
public class RandomLoadBalance implements LoadBalance{

	private final Random random = new Random();
	
	@Override
	public Invoker doSelect(List<Invoker> invokers) {
		int totalWeight = 0, len = invokers.size();
		boolean sameWeight = true;
		for(int i = 0; i < len; i++){
			int weight = invokers.get(i).getWeight();
			totalWeight += weight;
			if(sameWeight && i > 0 && weight != invokers.get(i - 1).getWeight()){
				//to mark whether invokers contains different weight
				sameWeight = Boolean.FALSE.booleanValue();
			}
		}
		if(totalWeight > 0 && !sameWeight){
			int offset = random.nextInt(totalWeight);
			for(int index = 0; index < len; index++){
				offset -= invokers.get(index).getWeight();
				if(offset < 0){
					return invokers.get(index);
				}
			}
		}
		return invokers.get(random.nextInt(len));
	}

}
