package lam.rpcframework.serialize;

import java.io.OutputStream;

import com.caucho.hessian.io.Hessian2Output;

/**
* <p>
* rpc Hessian2Output
* </p>
* @author linanmiao
* @date 2017年5月10日
* @version 1.0
*/
public class RpcHessian2Output extends Hessian2Output{

	public RpcHessian2Output(OutputStream os) {
		super(os);
	}

}
