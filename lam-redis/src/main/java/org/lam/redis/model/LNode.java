package org.lam.redis.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年1月12日
* @version 1.0
*/
public class LNode implements Serializable{

	private static final long serialVersionUID = -8882024750765150825L;
	
	private String key;
	
	private String keySplit;
	
	private String nodeSplit;
	
	private Object simpleValue;
	
	private Set<SNode> nodeValue;
	
	public boolean isSimpleValue(){
		return nodeValue == null || nodeValue.isEmpty();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKeySplit() {
		return keySplit;
	}

	public void setKeySplit(String keySplit) {
		this.keySplit = keySplit;
	}

	public String getNodeSplit() {
		return nodeSplit;
	}

	public void setNodeSplit(String nodeSplit) {
		this.nodeSplit = nodeSplit;
	}

	public Object getSimpleValue() {
		return simpleValue;
	}

	public void setSimpleValue(Object simpleValue) {
		this.simpleValue = simpleValue;
	}

	public Set<SNode> getNodeValue() {
		return nodeValue;
	}

	public void setNodeValue(Set<SNode> nodeValue) {
		this.nodeValue = nodeValue;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(key).append(keySplit);
		if(isSimpleValue()){
			sb.append(simpleValue);
		}else{
			Iterator<SNode> iter = nodeValue.iterator();
			while(true){
				sb.append(iter.next());
				if(!iter.hasNext()){
					break;
				}
				sb.append(nodeSplit);
			}
		}
		return sb.toString();
	}
	
}
