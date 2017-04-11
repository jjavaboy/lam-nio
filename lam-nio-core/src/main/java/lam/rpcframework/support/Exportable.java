package lam.rpcframework.support;
/**
* <p>
* export interface
* </p>
* @author linanmiao
* @date 2017年4月11日
* @version 1.0
*/
public interface Exportable {

	/**
	 * export a service
	 * @param object service object
	 * @param port export at the port
	 * @throws Exception
	 */
	public void export(final Object object, final int port)throws Exception;
	
}
