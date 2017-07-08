package lam.dubbo.provider.listener;

import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.ExporterListener;
import com.alibaba.dubbo.rpc.RpcException;

/**
* <p>
* export listener
* </p>
* @author linanmiao
* @date 2017年7月7日
* @version 1.0
*/
public class ServiceExportListener implements ExporterListener{

	@Override
	public void exported(Exporter<?> exporter) throws RpcException {
		//Class<?> clazz = exporter.getInvoker().getInterface();
		System.out.println(getClass().getName() + " export ");
	}

	@Override
	public void unexported(Exporter<?> exporter) {
		//Class<?> clazz = exporter.getInvoker().getInterface();
		System.out.println(getClass().getName() + " unexport ");
	}

}
