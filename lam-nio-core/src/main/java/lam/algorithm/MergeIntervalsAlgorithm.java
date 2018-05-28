package lam.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import lam.log.Console;
import lam.util.Gsons;

/**
* <p>
* 合并区间算法:<br/>
* 1.先根据每个区间的小值排序；
* 2.排好序后，再进行合并。
* </p>
* @author linanmiao
* @date 2018年5月25日
* @version 1.0
*/
public class MergeIntervalsAlgorithm {
	
	private class IntervalComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] a, int[] b) {
            //return a.start < b.start ? -1 : a.start == b.start ? 0 : 1;
        	return a[0] < b[0] ? -1 : a[0] == b[0] ? 0 : 1;
        }
    }

    public List<int[]> merge(List<int[]> intervals) {
        Collections.sort(intervals, new IntervalComparator());

        Console.println("sorted:" + Gsons.toJson(intervals));
        
        LinkedList<int[]> merged = new LinkedList<int[]>();
        for (int[] interval : intervals) {
            // if the list of merged intervals is empty or if the current
            // interval does not overlap with the previous, simply append it.
            //if (merged.isEmpty() || merged.getLast().end < interval.start) {
        	if (merged.isEmpty() || merged.getLast()[1] < interval[0]) {
                merged.add(interval);
            }
            // otherwise, there is overlap, so we merge the current and previous
            // intervals.
            else {
                //merged.getLast().end = Math.max(merged.getLast().end, interval.end);
            	merged.getLast()[1] = Math.max(merged.getLast()[1], interval[1]);
            }
        }

        return merged;
    }
	
	public static void main(String[] args) {
		List<int[]> list = new ArrayList<int[]>();
		list.add(new int[]{1, 3});
		list.add(new int[]{8, 10});
		list.add(new int[]{11, 23});
		list.add(new int[]{9, 12});
		list.add(new int[]{3, 5});
		list.add(new int[]{40, 100});
		list.add(new int[]{42, 45});
		
		MergeIntervalsAlgorithm algorithm = new MergeIntervalsAlgorithm();

		List<int[]> newList = algorithm.merge(list);
		Console.print(Gsons.toJson(newList));
	}

}
