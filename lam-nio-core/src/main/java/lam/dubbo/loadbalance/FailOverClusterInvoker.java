package lam.dubbo.loadbalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lam.dubbo.loadbalance.support.Invocation;
import lam.dubbo.loadbalance.support.Invoker;
import lam.dubbo.loadbalance.support.LoadBalance;
import lam.dubbo.loadbalance.support.RpcException;

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
	public boolean isAvailable() {
		return false;
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
		try {
			return doInvoke(localInvokerCache, invocation, loadBalance);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Object doInvoke(List<Invoker> invokers, Invocation invocation, LoadBalance loadBalance) throws Exception{
		int invokeLen = invocation.getRetryTimes() + 1;
		Throwable invokedThrowable = null;
		List<Invoker> selectedInvokers = new ArrayList<Invoker>(invokers.size());
		for(int i = 0; i < invokeLen; i++){
			Invoker invoker = doSelect(invokers, invocation, loadBalance, selectedInvokers);
			selectedInvokers.add(invoker);
			try{
				return invoker.invoke(invocation);
			}catch(RpcException e){
				e.printStackTrace();
				//throw e when e is business excetpion
				if(e.isBusinessException()){
					throw e;
				}
				invokedThrowable = e;
			}catch(Throwable t){
				t.printStackTrace();
				invokedThrowable = t;
			}
		}
		throw new Exception(
				String.format("Fail to invoke %s.%s", invocation.getInterface().getName(), invocation.getMethodName()));
	}
	
	private Invoker doSelect(List<Invoker> invokers, Invocation invocation, LoadBalance loadBalance, List<Invoker> selectedInvokers){
		if(invokers == null || invokers.isEmpty()){
			return null;
		}
		if(invokers.size() == 1){
			return invokers.get(0);
		}
		//只有2个invoker，退化成轮询
		if(invokers.size() == 2 && selectedInvokers != null && !selectedInvokers.isEmpty()){
			return invokers.get(0) == selectedInvokers.get(0) ? invokers.get(1) : invokers.get(0);
		}
		Invoker invoker = loadBalance.select(invokers);
		//如果提供者已经被调用过，则重新选择一个提供者
		if(selectedInvokers != null && selectedInvokers.contains(invoker)){
			return reSelect(invokers, invocation, loadBalance, selectedInvokers);
		}
		return null;
	}

	private Invoker reSelect(List<Invoker> invokers, Invocation invocation, LoadBalance loadBalance, List<Invoker> selectedInvokers) {
		
		List<Invoker> invokersExcludeOlds = new ArrayList<Invoker>(invokers.size());
		for(int i = 0; i < invokers.size(); i++){
			if(invokers.get(i).isAvailable() && !selectedInvokers.contains(invokers.get(i))){
				invokersExcludeOlds.add(invokers.get(i));
			}
		}
		if(!invokersExcludeOlds.isEmpty()){
			return loadBalance.select(invokersExcludeOlds);
		}
		return null;
	}

}
