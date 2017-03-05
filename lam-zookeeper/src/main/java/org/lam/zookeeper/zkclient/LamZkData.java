package org.lam.zookeeper.zkclient;

import java.io.Serializable;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月5日
* @versio 1.0
*/
public class LamZkData implements Serializable{

	private static final long serialVersionUID = 4938454984414738581L;

	private String serviceName;
	
	private String serviceIp;
	
	private int weight;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceIp() {
		return serviceIp;
	}

	public void setServiceIp(String serviceIp) {
		this.serviceIp = serviceIp;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
}
