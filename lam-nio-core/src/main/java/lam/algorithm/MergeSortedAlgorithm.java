package lam.algorithm;

import lam.log.Console;
import lam.util.Gsons;

/**
* <p>
* take time in stable:<br/>
* O(nlog2n)<br/>
* 分而治之的思想
* </p>
* @author linanmiao
* @date 2018年5月27日
* @versio 1.0
*/
public class MergeSortedAlgorithm {
	
	public int[] sort(int[] ints) {
		return sort0(ints, 0, ints.length - 1);
	}
	
	private int[] sort0(int[] ints, int start, int end) {
		if (start > end) {
			throw new IllegalArgumentException(start + " greater than " + end);
		}
		if (start < end) {
			int middle = (start + end) / 2;
			int[] ints1 = sort0(ints, start, middle); //分
			int[] ints2 = sort0(ints, middle + 1, end); //分
			int[] mergeInts = merge(ints1, 0, ints1.length - 1, ints2, 0, ints2.length - 1); //治
			Console.println(Gsons.toJson(ints1) + "+" + Gsons.toJson(ints2) + "->" + Gsons.toJson(mergeInts));
			return mergeInts;
		} else { //means: ints has only one element.
			return new int[]{ints[start]};
		}
		
	}
	
	private int[] merge(int[] a, int aStart, int aEnd, int[] b, int bStart, int bEnd) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("int[] a, " + aStart + " greater than " + aEnd);
		}
		if (bStart > bEnd) {
			throw new IllegalArgumentException("int[] b, " + bStart + " greater than " + bEnd);
		}
		int[] newInts = new int[(aEnd - aStart + 1) + (bEnd - bStart + 1)];
		int newStart = 0, newEnd = newInts.length - 1;
		//merge int[] a and int[] b int int[] newInts
		while (aStart <= aEnd) {
			boolean hasB = false;
			while (aStart <= aEnd && bStart <= bEnd) {
				hasB = true;
				if (a[aStart] >= b[bStart]) {
					newInts[newStart++] = b[bStart++];
				} else {
					newInts[newStart++] = a[aStart++];
				}
			}
			if (!hasB) {
				newInts[newStart++] = a[aStart++];
			}
		}
		//执行到这里，则a数组已经循环完了。
		while (bStart <= bEnd) {
			if (newStart <= newEnd) {
				newInts[newStart++] = b[bStart++];
			} else {
				bStart++;
			}
		}
		return newInts;
	}
	
	public static void main(String[] args) {
		int[] sources = {3, 1, 10, 2, 5, 4, 11, 33, 5};
		int[] newSources = new MergeSortedAlgorithm().sort(sources);
		Console.println(Gsons.toJson(newSources));
	}

}
