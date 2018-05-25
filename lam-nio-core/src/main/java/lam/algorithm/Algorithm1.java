package lam.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lam.log.Console;
import lam.util.Gsons;

/**
* <p>
* @todo 
* to be finish
* </p>
* @author linanmiao
* @date 2018年5月25日
* @version 1.0
*/
public class Algorithm1 {
	
	private static class Node {
		final int index;
		final int min;
		final int max;
		Node(int index, int min, int max) {
			this.index = index;
			this.min = min;
			this.max = max;
		}
	}
	
	private static class Link {
		Link prev;
		int number;
		Node node;
		Link next;
	}
	
	public List<int[]> init() {
		List<int[]> list = new ArrayList<int[]>();
		list.add(new int[]{1, 3});
		list.add(new int[]{8, 10});
		list.add(new int[]{11, 23});
		list.add(new int[]{9, 12});
		list.add(new int[]{3, 5});
		list.add(new int[]{40, 100});
		list.add(new int[]{42, 45});
		return list;
	}
	
	public List<int[]> merge(List<int[]> list) {
		List<int[]> perferList = new ArrayList<int[]>();
		for (int[] ints : list) {
			merge(ints, perferList);
		}
		return perferList;
	}
	
	private void merge(int[] source, List<int[]> targetList) {
		if (targetList.isEmpty()) {
			targetList.add(source);
			return ;
		}
		Iterator<int[]> iter = targetList.iterator();
		while (iter.hasNext()) {
			int[] target = iter.next();
			List<int[]> newList = merge(source, target);
			if (newList.size() == 1) {
				iter.remove();
			}
			//add2List(newList, targetList);
		}
	}
	
	/*private void add2List(List<int[]> fromList, List<int[]> toList) {
		for (int[] from : fromList) {
			boolean exists = false;
			for (int[] to : toList) {
				if (from[0] == to[0] && from[1] == to[1]) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				toList.add(from);
			}
		}
	}*/
	
	private List<int[]> merge(int[] source, int[] target) {
		if (source[0] > source[1]) {
			throw new IllegalArgumentException(String.format("source[0]:%d greater than source[1]:%d", source[0], source[1]));
		}
		if (target[0] > target[1]) {
			throw new IllegalArgumentException(String.format("target[0]:%d greater than target[1]:%d", target[0], target[1]));
		}
		List<int[]> list = new ArrayList<int[]>();
		if (source[1] < target[0]) {
			//has not common area
			list.add(source);
			list.add(target);
		} else if (source[1] == target[0]) {
			list.add(new int[]{source[0], target[1]});
		} else { //means: source[1] > target[0]
			if (source[1] <= target[1]) {
				if (source[0] <= target[0]) {					
					list.add(new int[]{source[0], target[1]});
				} else { //means: source[0] > target[0]
					list.add(new int[]{target[0], target[1]});
				}
			} else { //means: source[1] > target[1], to compare source[0] and target[1], compare source[0] and target[0]
				//1.compare source[0] and target[1]
				if (source[0] > target[1]) {
					//has not common area
					list.add(source);
					list.add(target);
				} else if (source[0] == target[1]){
					list.add(new int[]{target[0], source[1]});
				} else { //means: source[0] < target[1]
					//2.compare source[0] and target[0]
					if (source[0] >= target[0]) {
						list.add(new int[]{target[0], source[1]});
					} else { //means: source[0] < target[0]
						list.add(new int[]{source[0], source[1]});
					}
				}
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		Algorithm1 algorithm = new Algorithm1();
		List<int[]> list = algorithm.init();
		List<int[]> newList = algorithm.merge(list);
		Console.print(Gsons.toJson(newList));
	}

}
