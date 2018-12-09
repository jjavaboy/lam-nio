package lam.dubbo.consumer.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcResult;

/**
* <p>
* 失败时的Command
* </p>
* @author linanmiao
* @date 2017年7月28日
* @version 1.0
*/
@Activate(group = {Constants.CONSUMER}, value = "fallback")
public class FallbackHystrixFilter extends BaseFallbackHystrixFilter{
	
	private static Logger logger = LoggerFactory.getLogger(FallbackHystrixFilter.class);
	
	public FallbackHystrixFilter(){
		super("FallbackHystrix", new Callback(){
			@Override
			public Result call(Invoker<?> invoker, Invocation invocation) {
				/*if("login".equals(invocation.getMethodName()) &&
				   "lam.dubbo.api.LoginService".equals(invoker.getInterface().getName())){
					Result result = new RpcResult(false);
					logger.info(String.format("%s.%s fail, return %s", invoker.getInterface().getName(), invocation.getMethodName(), result));
					return result;
				}
				return null;*/
				return invoker.invoke(invocation);
			}});
		logger.info(getClass().getName() + " constructor");
	}

}
