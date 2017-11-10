package lam.dubbo.provider.api.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.dubbo.rpc.cluster.Merger;

import lam.log.Console;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月10日
* @version 1.0
*/
public class MyMerger implements Merger<List<Integer>>{

	@Override
	public List<Integer> merge(List<Integer>... items) {
		List<Integer> result = new ArrayList<Integer>(items.length);
		for (int i = 0; i < items.length; i++) {
			result.addAll(items[i]);
		}
		Console.println("" + result);
		return result;
	}

}
