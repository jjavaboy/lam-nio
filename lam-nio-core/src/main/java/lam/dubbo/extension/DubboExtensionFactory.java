package lam.dubbo.extension;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月3日
* @version 1.0
*/
@DubboSPI
public interface DubboExtensionFactory {

	/**
	 * 
	 * @param type object class type
	 * @param name object class name 
	 * @return instance of object class
	 */
	<T> T getExtension(Class<T> type, String name);
	
}
