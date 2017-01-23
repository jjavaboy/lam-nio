package lam.concurrent;

import java.io.File;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年1月22日
* @version 1.0
*/
public class CalculatePlatform {
	
	private final AtomicLong fileCount = new AtomicLong();
	private final AtomicLong size = new AtomicLong();
	private final CountDownLatch latch = new CountDownLatch(1);
	private final ExecutorService executorService;
	
	public CalculatePlatform(int nThreads){
		executorService = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	public void calculate(String[] filenames){
		for(String filename : filenames){
			calculate(filename);
		}
	}
	
	public void calculate(String filename){
		if(filename != null && !filename.equals("")){			
			calculate(new File(filename));
		}
	}
	
	public void calculate(File file){
		if(file.isFile()){
			size.addAndGet(file.length());
		}else{
			calculate(file.listFiles());
		}
	}
	
	public void calculate(File[] files){
		if(files == null){
			return ;
		}
		for(final File f : files){
			if(f != null){
				fileCount.incrementAndGet();
				executorService.execute(new Runnable(){
					@Override
					public void run() {
						calculate(f);
						if(fileCount.decrementAndGet() == 0){
							latch.countDown();
						}
					}});
			}
		}
	}
	
	public long getSize(){
		try{
			latch.await(60 * 2, TimeUnit.SECONDS);
		}catch(InterruptedException i){
			i.printStackTrace();
		}
		return size.get();
	}
	
	public void destroy(){
		executorService.shutdown();
	}
	
	/**
	 * size:52805488133
	 * time:11771
	 *
	 * size:52806410980
     * time:11731
     * 
     * size:52805476862
	 * time:12355
	 */
	public static void main(String[] args){
		int nThreads = Runtime.getRuntime().availableProcessors();
		if(args != null && args.length > 0){
			try{
				nThreads = Integer.parseInt(args[0]);
				System.out.println("args:" + args[0]);
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
		
		long start = System.nanoTime();
		CalculatePlatform calculatePlatform = new CalculatePlatform(nThreads);
		calculatePlatform.calculate(new String[]{"C:\\", "D:\\"});
		System.out.println("size:" + calculatePlatform.getSize());
		System.out.println("time:" + (System.nanoTime() - start) / 1.0e9);
		System.out.println("nThread:" + nThreads);
		calculatePlatform.destroy();
	}

}
