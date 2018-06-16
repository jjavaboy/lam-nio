package lam.spring.boot.service.impl;

import org.springframework.stereotype.Service;

import lam.spring.boot.service.IFooService;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月16日
* @versio 1.0
*/
@Service
public class FooService implements IFooService {

	@Override
	public boolean dodo(int i) {
		return (i % 2) == 0;
	}


}
