package lam.automaton;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

/**
* <p>
* 确定有穷自动机
* </p>
* @author linanmiao
* @date 2017年4月26日
* @version 1.0
*/
public class DeterministicFiniteAutomaton {
	
	private Map<Character, Node> map;
	
	public DeterministicFiniteAutomaton(){
		map = new HashMap<Character, Node>();
	}
	
	public void load(Collection<String> c){
		if(c == null || c.isEmpty()){
			return ;
		}
		Iterator<String> iter = c.iterator();
		while(iter.hasNext()){
			Map<Character, Node> currentMap = map;
			String s = iter.next();
			for(int idx = 0; ; idx++){
				Node node = currentMap.get(s.charAt(idx));
				if(node == null){
					node = new Node();
					node.end = false;
					currentMap.put(s.charAt(idx), node);
				}
				if(idx < s.length() - 1){
					if(node.next == null){						
						node.next = new HashMap<Character, Node>(1, 1.0F);
					}
					currentMap = node.next; //current map point to the 'next' of node
				}else{
					node.end = true;
					break;					
				}
			}
		}
	}
	
	public Set<String> has(String str){
		if(str == null || str.isEmpty()){
			return null;
		}
		Set<String> set = new HashSet<String>();
		int lastIndex = str.length() - 1;
		for(int idx = 0; idx <= lastIndex; idx++){			
			has(map, set, str, idx, lastIndex);
		}
		return set;
	}
	
	private void has(Map<Character, Node> map, Set<String> set, String str, int fromIndex, int toIndex){
		if(fromIndex > toIndex){
			throw new IllegalArgumentException("fromIndex(" + fromIndex + ") is larger than toIndex(" + toIndex + ")");
		}
		int hasCnt = 0;
		Map<Character, Node> currentMap = map;
		for(int idx = fromIndex; idx <= toIndex; idx++){
			Node node = currentMap.get(str.charAt(idx));
			if(node == null){
				break;
			}
			hasCnt++;
			if(node.end){//visit to the 'end', then take the sensitive into the set.
				set.add(str.substring(fromIndex, fromIndex + hasCnt));
				if(node.next == null){//'end' is true and 'next' is null, it means the automaton is in the end really.
					break;
				}
			}
			currentMap = node.next;
		}
	}

	private class Node{
		
		boolean end;
		
		Map<Character, Node> next;
		
		Node(){}		
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof Node)){
				return false;
			}
			Node that = (Node) obj;
			return this.end = that.end && ((this.next != null && this.next.equals(that.next)) || this.next == that.next);
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{")
			.append("end").append("=").append(end).append(", ")
			.append("next").append("=").append(next)
			.append("}");
			return sb.toString();
		}
		
	}
	
	public static void main(String[] args){
		String[] str = {
				"你",
				"我爱你",
				"你爱我我我",
				"我不爱你你",
				"你不爱我我我"
		};
		List<String> list = Arrays.asList(str);
		DeterministicFiniteAutomaton dfa = new DeterministicFiniteAutomaton();
		dfa.load(list);
		System.out.println(new Gson().toJson(dfa.map));
		
		String s = "111组织在我不爱你你1我爱你11,2w2w我你爱我我我你不爱我我我11";
		System.out.println(dfa.has(s));
	}
	
}
