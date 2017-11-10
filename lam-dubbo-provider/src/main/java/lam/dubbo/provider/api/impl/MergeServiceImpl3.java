package lam.dubbo.provider.api.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lam.dubbo.api.MergeService;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月10日
* @version 1.0
*/
public class MergeServiceImpl3 implements MergeService{

	@Override
	public List<String> mergerResult() {
		List<String> menus = new ArrayList<String>(2);
		menus.add("group-3.1");
		menus.add("group-3.2");
		return menus;
	}

	@Override
	public List<Integer> mergerResult2() {
		List<Integer> menus = Arrays.asList(31, 32);
		return menus;
	}

}
