package org.lam.redis.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年1月12日
* @version 1.0
*/
public class SNode implements Serializable{

	private static final long serialVersionUID = 7358063136734346712L;
	
	public final static char DEFAULT_SPLIT = '=';
	
	private String key;
	
	private String value;
	
	private char split;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public char getSplit() {
		return split;
	}

	public void setSplit(char split) {
		this.split = split;
	}
	
	public static SNode parse(String str){
		return parse(str, DEFAULT_SPLIT);
	}
	
	public static SNode parse(String str, char split){
		int idx = -1;
		if(str == null || (idx = str.indexOf(split)) == -1){
			throw new RuntimeException(String.format("str:%s\n str is null or str does not contains split[%c]", str, split));
		}
		SNode snode = new SNode();
		snode.setKey(str.substring(0, idx));
		snode.setSplit(split);
		snode.setValue(str.substring(idx + 1));
		return snode;
	}
	
	public static Set<SNode> parseToSet(String str, String snodeSplit){
		return parseToSet(str, snodeSplit, DEFAULT_SPLIT);
	}
	
	public static Set<SNode> parseToSet(String str, String snodeSplit, char split){
		String[] snodeStrs = str.split(snodeSplit);
		Set<SNode> set = new HashSet<SNode>();
		for(String snodeStr : snodeStrs){
			snodeStr = snodeStr.trim();
			if(snodeStr.indexOf(split) != -1){
				set.add(parse(snodeStr));
			}
		}
		return set;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		if(key != null){
			hash &= key.hashCode();
		}
		hash &= split;
		if(value != null){
			hash &= value.hashCode();
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof SNode)){
			return false;
		}
		SNode snode = (SNode) obj;
		if((key == snode.getKey()) || (key != null && key.equals(snode.getKey())) && 
		    split == snode.getSplit() &&
		   (value == snode.getValue()) || (value != null && value.equals(snode.getValue()))){
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return new StringBuilder(key).append(split).append(value).toString();
	}
	
}
