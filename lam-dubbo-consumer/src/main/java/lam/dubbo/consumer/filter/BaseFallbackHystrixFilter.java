package lam.dubbo.consumer.filter;

import java.util.Objects;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
* <p>
* BaseHystrixCommandFilter
* </p>
* @author linanmiao
* @date 2017年7月28日
* @version 1.0
*/
public abstract class BaseFallbackHystrixFilter implements Filter{
	
	protected static final String DEFAULT_COMMAND_GROUP_KEY = "Hystrix";
	
	protected Setter setter;
	
	protected Callback call;
	
	public BaseFallbackHystrixFilter(String commandKey, Callback call){
		this(commandKey, DEFAULT_COMMAND_GROUP_KEY, call);
	}
	
	public BaseFallbackHystrixFilter(String commandKey, String commandGroupKey, Callback call){
		this(createSetter(commandKey, commandGroupKey), call);
	}
	
	public BaseFallbackHystrixFilter(HystrixCommand.Setter setter, Callback call){
		Objects.requireNonNull(setter, "parameter:setter can not be null");
		Objects.requireNonNull(call, "parameter:call can not be null");
		this.setter = setter;
		this.call = call;
	}
	
	protected static Setter createSetter(String commandKey, String commandGroupKey){
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

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		//Hystrix 每个Command只能execute一次，所以每次invoke都需要一个新的Command
		return new DefaultHystrixCommand(setter, invoker, invocation, call).execute();
	}
	
	static class DefaultHystrixCommand extends HystrixCommand<Result>{
		
		protected Invoker<?> invoker;
		
		protected Invocation invocation;
		
		protected Callback call;
		
		public DefaultHystrixCommand(HystrixCommand.Setter setter, Invoker<?> invoker, Invocation invocation, Callback call){
			super(setter);
			this.invoker = invoker;
			this.invocation = invocation;
			this.call = call;
		}
		
		@Override
		protected Result run() throws Exception {
			return invoker.invoke(invocation);
		}
		
		@Override
		protected Result getFallback() {
			return call.call(invoker, invocation);
		}
	}

}
