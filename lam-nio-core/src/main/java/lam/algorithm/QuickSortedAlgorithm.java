package lam.algorithm;

import lam.log.Console;
import lam.util.Gsons;

/**
* <p>
* take time:O(nlgn)
* </p>
* @author linanmiao
* @date 2018年5月26日
* @versio 1.0
*/
public class QuickSortedAlgorithm {
	
	public int[] sort(int[] ints) {
		return sort(0, ints.length - 1, ints);
	}
	
	private int[] sort(int left, int right, int[] ints) {
		if (left < right) {
			int middle = sort0(left, right, ints);
			sort(left, middle - 1, ints);
			sort(middle + 1, right, ints);
		}
		return ints;
	}
	
	private int sort0(int left, int right, int[] ints) {
		//take ints[left] as the base mark in default.
		//相当于先挖空ints[left]的数据出来先。
		int base = ints[left];
		while (left < right) {
			while (left < right && ints[right] >= base) {
				right--;
			}
			//take the smaller number to the left part.
			ints[left] = ints[right];
			while (left < right && ints[left] <= base) {
				left++;
			}
			//task the greater num to the right part.
			ints[right] = ints[left];
		}
		ints[left] = base;
		return left;
	}
	
	public static void main(String[] args) {
		int[] sources = {3, 1, 10, 2, 5, 4, 11, 33, 5};
		int[] newSources = new QuickSortedAlgorithm().sort(sources);
		Console.println(Gsons.toJson(newSources));
	}

}
