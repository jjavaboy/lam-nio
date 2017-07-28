package lam.dubbo.consumer.filter;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;

/**
* <p>
* callback
* </p>
* @author linanmiao
* @date 2017年7月28日
* @version 1.0
*/
public interface Callback{
	public Result call(Invoker<?> invoker, Invocation invocation);
}
