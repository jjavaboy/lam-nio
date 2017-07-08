package lam.dubbo.provider.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

/**
* <p>
* before filter
* </p>
* @author linanmiao
* @date 2017年7月7日
* @version 1.0
*/
@Activate(group = {Constants.PROVIDER}, value = "beforeFilter")
public class BeforeFilter implements Filter{

	public BeforeFilter(){
		System.out.println(getClass().getName() + " constructor");
	}
	
	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		System.out.println(String.format("%s.%s(%s) before.", 
				invocation.getClass().getName(), invocation.getMethodName(), invocation.getParameterTypes()));
		return invoker.invoke(invocation);
	}

}
