package lam.dubbo.api;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年2月26日
* @versio 1.0
*/
public interface LoginService {
	
	public boolean login(String username);
	
	public boolean logout(String username);

}
