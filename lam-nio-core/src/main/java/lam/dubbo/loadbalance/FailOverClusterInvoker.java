package lam.dubbo.loadbalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lam.dubbo.loadbalance.support.Invocation;
import lam.dubbo.loadbalance.support.Invoker;
import lam.dubbo.loadbalance.support.LoadBalance;

/**
* <p>
* failover invoker
* </p>
* @author linanmiao
* @date 2017年5月2日
* @versio 1.0
*/
public class FailOverClusterInvoker implements Invoker{
	
	private List<Invoker> invokers;
	private Map<String, List<Invoker>> localCacheMethodInvokers;
	
	public FailOverClusterInvoker(List<Invoker> invokers){
		this.invokers = invokers;
		this.localCacheMethodInvokers = toMap(this.invokers);
	}
	
	@Override
	public Class<?> getInvokerClass() {
		return null;
	}

	@Override
	public int getWeight() {
		return 0;
	}

	@Override
	public long getTimestamp() {
		return 0;
	}
	
	private Map<String, List<Invoker>> toMap(List<Invoker> invokers){
		return null;
	}
	
	private String toMethodInvokerKey(final Invocation invocation){
		return invocation.getMethodName() + ":" + invocation.getParameterTypes();
	}

	@Override
	public Object invoke(final Invocation invocation) {
		List<Invoker> localInvokerCache = this.localCacheMethodInvokers.get(toMethodInvokerKey(invocation));
		LoadBalance loadBalance = new RandomLoadBalance();
		return doInvoke(localInvokerCache, invocation, loadBalance);
	}
	
	public Object doInvoke(List<Invoker> list, Invocation invocation, LoadBalance loadBalance){
		Throwable invokedThrowable = null;
		List<Invoker> selectedInvokers = new ArrayList<Invoker>();
		return null;
	}

}
