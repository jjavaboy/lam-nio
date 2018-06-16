package lam.spring.boot.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月16日
* @versio 1.0
*/
@XmlRootElement
public class MyThing {
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	public MyThing setName(String name) {
		this.name = name;
		return this;
	}
}
