package lam.rpcframework.support;
/**
* <p>
* refer interface
* </p>
* @author linanmiao
* @date 2017年4月11日
* @version 1.0
*/
public interface Referable {
	
	/**
	 * reference service
	 * @param interface
	 * @param host
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public <T> T refer(final Class<T> interfaceClass, final String host, final int port) throws Exception;

}
