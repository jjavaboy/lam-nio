package lam.algorithm;

import lam.log.Console;
import lam.util.Gsons;

/**
* <p>
* take time:<br/>
* best case:O(n)<br/>
* worst case:O(n ^ 2)<br/>
* </p>
* @author linanmiao
* @date 2018年5月26日
* @versio 1.0
*/
public class InsertSortedAlgorithm {
	
	public int[] sort(int[] ints) {
		for (int currentIndex = 1, fromIndex = 0; currentIndex <= ints.length - 1; currentIndex++) {
			int toIndex = currentIndex;
			if (toIndex <= ints.length - 1) {
				sort0(currentIndex, fromIndex, toIndex, ints);
			}
		}
		return ints;
	}
	/**
	 * elements in <code>fromIndex<code/> to <code>toIndex<code/> of <code>ints<code/> are sorted.
	 * @param currentIndex
	 * @param fromIndex
	 * @param toIndex
	 * @param ints
	 */
	private void sort0(int currentIndex, int fromIndex, int toIndex, int[] ints) {
		int e = ints[currentIndex];
		while (fromIndex < toIndex) {
			if (e < ints[fromIndex]) {
				//move the elements fromIndex to toIndex
				while (fromIndex < toIndex) {
					ints[toIndex] = ints[--toIndex];
				}
				ints[fromIndex] = ints[currentIndex];
				break ;
			}
			fromIndex++;
		}
	}
	
	public static void main(String[] args) {
		int[] sources = {3, 1, 10, 2, 5, 4, 11, 33, 5};
		int[] newSources = new InsertSortedAlgorithm().sort(sources);
		Console.println(Gsons.toJson(newSources));
	}

}
