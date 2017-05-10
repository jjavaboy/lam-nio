package lam.rpcframework.serialize;

import java.io.InputStream;

import com.caucho.hessian.io.Hessian2Input;

/**
* <p>
* rpc Hessain2Input
* </p>
* @author linanmiao
* @date 2017年5月10日
* @version 1.0
*/
public class RpcHessian2Input extends Hessian2Input{

	public RpcHessian2Input(InputStream is) {
		super(is);
	}

}
