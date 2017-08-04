package lam.dubbo.provider.hystrix;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

import rx.functions.Action1;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年7月18日
* @version 1.0
*/
public class HelloWorldCommand extends HystrixCommand<String>{
	
	private String name;
	
	private static String commandGroupKey = "LAM";
	
	public HelloWorldCommand(String name){
		//设置配置
		super(createSetter(name));
		this.name = name;
	}
	
	private static Setter createSetter(String commandKey){
		Setter setter = HystrixCommand.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey(commandGroupKey))  //组Group的key，一般相同的组，使用相同线程池
				.andCommandKey(HystrixCommandKey.Factory.asKey(commandKey)) //每个Command的key，默认:getClass().getSimpleName();
				//.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("")) //线程池的key，一般来说，相同的组使用相同线程池，不用设置线程池的key，设置组名就行了。
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(20)) //服务线程池数量
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionTimeoutEnabled(false) //禁用Hystrix的超时，比如在dubbo服务中本身有超时设置，则禁用这个超时，使用dubbo的超时就可以了
						//.withExecutionTimeoutInMilliseconds(10000) //Hystrix的超时时长设置，禁用了Hystrix本身的超时，则这项不用设置
						.withMetricsRollingStatisticalWindowInMilliseconds(10000) //metrics的统计窗口时长，默认10秒，使用默认值，一般不用设置
						.withMetricsRollingStatisticalWindowBuckets(10) //metrics的统计窗口桶数，默认10个，使用默认值，一般不用设置
						.withCircuitBreakerRequestVolumeThreshold(20) //在一个统计窗口里，默认至少有20个请求，按照统计窗口时长的默认设置，即是10秒至少有20个请求
						.withCircuitBreakerSleepWindowInMilliseconds(30000) //熔断器打开到关闭的时间窗长度，熔断器30秒后再去调用服务，默认5秒
						.withCircuitBreakerErrorThresholdPercentage(60) //熔断器关闭到打开阈值:60%失败
						);
		//HystrixCommandProperties的详细配置，可以查看com.netflix.hystrix.HystrixCommandProperties，这个类的相关配置设在在内部类Setter
		return setter;
	}
	
	/**
	 * metrics
	 * Hystrix offers metrics per command key and to very fine granularities (on the order of seconds).
	 * Hystrix为每个command key提供metrics，并以秒为单位，作为非常好的度量在metrics中统计。
	 * Hystrix会将metrics的统计信息保存在内存里（in-memory data structures），这样，可以在需要的时候查询。
	 */

	@Override
	protected String run() throws Exception {
		return String.format("hello:%s, thread:%s", this.name, Thread.currentThread().getName());
	}
	
	public static void main(String[] args) {
		helloworld();
		subscribe();
		fallback();
		semaphore();
		fallbackNesting();
	}
	
	private static void helloworld(){
		System.out.println("------------");
		//一个命令只能调用一次
		HelloWorldCommand synchronizedCommand = new HelloWorldCommand("synchronized-hystrix");
		//使用execute()同步调用代码,效果等同于:helloWorldCommand.queue().get();
		String rs = synchronizedCommand.execute();
		System.out.println("result(synchronized):" + rs);
		
		HelloWorldCommand asynchronizedCommand = new HelloWorldCommand("asynchronized-hystrix");
		//异步调用
		Future<String> future = asynchronizedCommand.queue();
		try {
			//get()操作不能超过command定义的超时时间,默认:1秒 
			rs = future.get(100, TimeUnit.MILLISECONDS);
			System.out.println("result(asynchronized):" + rs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	private static void subscribe(){
		System.out.println("------------");
		HelloWorldCommand subscribeCommand = new HelloWorldCommand("subscribe-hystrix");
		rx.Observable<String> ob = subscribeCommand.observe();
		//注册结果回调事件
		ob.subscribe(new Action1<String>(){
			@Override
			public void call(String arg0) {
				System.out.println("call:" + arg0);
			}});
		//注册完整执行生命周期事件
		ob.subscribe(new rx.Observer<String>(){
			@Override
			public void onCompleted() {
				// onNext/onError完成之后最后回调  
                System.out.println("execute onCompleted");
			}
			@Override
			public void onError(Throwable e) {
				 // 当产生异常时回调  
                System.out.println("onError " + e.getMessage());  
                e.printStackTrace();  
			}
			@Override
			public void onNext(String arg0) {
				// 获取结果后回调  
                System.out.println("onNext: " + arg0);
			}});
	}
	
	private static void fallback(){
		System.out.println("------------");
		//配置依赖超时时间: 500ms
		FallbackCommand fallbackCommand = new FallbackCommand("fallback-hystrix", 500, 800);
		//run方法sleep 800ms所以会超时，调用getFallback方法 返回结果。
		String rs = fallbackCommand.execute();
		System.out.println("result:" + rs);
	}
	
	private static void semaphore(){
		System.out.println("------------");
		SemaphoreCommand semaphoreCommand = new SemaphoreCommand("semaphoreHystrix");
		String rs = semaphoreCommand.execute();
		System.out.println("result:" + rs);
	}
	
	/** fallback嵌套 */
	private static void fallbackNesting(){
		System.out.println("------------");
		FallbackNestingCommand fallbackNestingCommand = new FallbackNestingCommand("fallbackNestingHystrix");
		String rs = fallbackNestingCommand.execute();
		System.out.println("result:" + rs);
	}
	
	static class FallbackCommand extends HystrixCommand<String>{
		private String name;
		private int runTimeout;
		
		public FallbackCommand(String name, int timeout, int runTimeout){
			//配置依赖超时时间: 500ms, 默认线程池-10个线程
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FallbackGroup"))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeout))
					.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(10)));
			this.name = name;
			this.runTimeout = runTimeout;
		}

		@Override
		protected String run() throws Exception {
			//sleep for 800ms
			//除了HystrixBadRequestException异常之外，所有从run()方法抛出的异常都算作失败，并触发降级getFallback()和断路器逻辑
			TimeUnit.MILLISECONDS.sleep(runTimeout);
			return String.format("hello:%s, thread:%s", this.name, Thread.currentThread().getName());
		}
		
		@Override
		protected String getFallback() {
			return "execute fall";
		}
	}
	
	static class SemaphoreCommand extends HystrixCommand<String>{
		private String name;
		
		public SemaphoreCommand(String name){
			/* 配置信号量隔离方式,默认采用线程池隔离 */
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(SemaphoreCommand.class.getSimpleName()))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationStrategy(
							HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));
			this.name = name;
		}

		@Override
		protected String run() throws Exception {
			return "Hystrix:" + name + ", Thread:" + Thread.currentThread().getName();
		}
	}

	static class FallbackNestingCommand extends HystrixCommand<String>{
		private String name;
		public FallbackNestingCommand(String name){
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX"))  
	                .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueCommand"))); 
			this.name = name;
		}
		@Override
		protected String run() throws Exception {
			throw new RuntimeException("throw an exception manually.");
		}
		protected String getFallback(){
			return new FallbackNestingWorkerCommand(name).execute();
		}
	}
	static class FallbackNestingWorkerCommand extends HystrixCommand<String>{
		private String name;
		public FallbackNestingWorkerCommand(String name){
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX"))  
                    .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueFallbackCommand"))  
                    // 使用不同的线程池做隔离，防止上层线程池跑满，影响降级逻辑.  
                    .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("RemoteServiceXFallback")));
			this.name = name;
		}
		@Override
		protected String run() throws Exception {
			System.out.println(getClass().getName() + " run, name:" + this.name);
			return "I'm from " + getClass().getSimpleName();
		}
	}
}
