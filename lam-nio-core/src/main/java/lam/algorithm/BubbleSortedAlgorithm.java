package lam.algorithm;

import lam.log.Console;
import lam.util.Gsons;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年5月25日
* @version 1.0
*/
public class BubbleSortedAlgorithm {
	
	public int[] sort(int[] sources) {
		for (int i = 0, lastIndex = sources.length - 1; i < lastIndex; lastIndex--) {
			sort(i, lastIndex, sources);
		}
		return sources;
	}
	
	public void sort(int start, int end, int[] sources) {
		if (start >= end) {
			return ;
		}
		while (start < end) {
			if (sources[start] > sources[start + 1]) {
				int temp = sources[start];
				sources[start] = sources[start + 1];
				sources[start + 1] = temp;
			}
			start++;
		}
	}
	
	public static void main(String[] args) {
		int[] sources = {3, 1, 10, 2, 0, 4, 11, 33, 5};
		int[] newSources = new BubbleSortedAlgorithm().sort(sources);
		Console.println(Gsons.toJson(newSources));
	}

}
