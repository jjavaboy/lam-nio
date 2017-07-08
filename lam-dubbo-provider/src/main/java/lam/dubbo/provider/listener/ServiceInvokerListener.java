package lam.dubbo.provider.listener;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.InvokerListener;
import com.alibaba.dubbo.rpc.RpcException;

/**
* <p>
* service invoker listener
* </p>
* @author linanmiao
* @date 2017年7月7日
* @version 1.0
*/
public class ServiceInvokerListener implements InvokerListener{

	@Override
	public void referred(Invoker<?> invoker) throws RpcException {
		System.out.println(getClass().getName() + " refer");
	}

	@Override
	public void destroyed(Invoker<?> invoker) {
		System.out.println(getClass().getName() + " destroy");
	}

}
