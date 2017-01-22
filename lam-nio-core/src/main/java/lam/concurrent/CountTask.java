package lam.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年1月22日
* @version 1.0
*/
public class CountTask extends RecursiveTask<Integer>{
	
	private static final long serialVersionUID = -5562742348103637026L;

	private static final int threshold = 2;
	
	private int start;
	
	private int end;
	
	public CountTask(int start, int end){
		this.start = start;
		this.end = end;
		System.out.println(String.format("start:%d, end:%d", start, end));
	}

	@Override
	protected Integer compute() {
		int sum = 0;
		boolean canCompute = (end - start) <= threshold;
		if(canCompute){
			for(int idx = start; idx <= end; idx++){
				sum += idx;
			}
		}else{
			int middle = (end + start) / 2;
			CountTask leftCountTask = new CountTask(start, middle);
			CountTask rightCountTask = new CountTask(middle + 1, end);
			
			leftCountTask.fork();
			rightCountTask.fork();
			
			int leftResult = leftCountTask.join();
			int rightResult = rightCountTask.join();
			
			sum = leftResult + rightResult;
		}
		return sum;
	}
	
	public static void main(String[] args) throws Exception{
		ForkJoinPool pool = new ForkJoinPool();
		
		/*Future<Integer> result = pool.submit(new CountTask(1, 10));
		System.out.println("result:" + result.get());*/

		int result = pool.invoke(new CountIntegerTask(1, 10));
		System.out.println("result:" + result);
	}
	
	private static class CountIntegerTask extends RecursiveTask<Integer>{
		
		private static final long serialVersionUID = 52785386388661146L;
		
		private static final int THREHOLD = 2;
		private int start;
		private int end;
		
		private CountIntegerTask(int start, int end){
			this.start = start;
			this.end = end;
		}

		@Override
		protected Integer compute() {
			int sum = 0;
			boolean canCompute = (end - start) <= THREHOLD;
			if(canCompute){
				for(int idx = start; idx <= end; idx++){
					sum += idx;
				}
			}else{
				List<CountIntegerTask> list = new ArrayList<CountIntegerTask>();
				int middle = (end + start) / 2;
				list.add(new CountIntegerTask(start, middle));
				list.add(new CountIntegerTask(middle + 1, end));
				
				for(CountIntegerTask task : invokeAll(list)){
					sum += task.join();
				}
			}
			return sum;
		}
		
	}

}
