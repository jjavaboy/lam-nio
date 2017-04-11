import lam.rpcframework.ExportFramework;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月11日
* @version 1.0
*/
public class RpcFrameworkServiceTest {
	
	public static void main(String[] args){
		UserService userService = new UserServiceImpl();
		try {
			new ExportFramework().export(userService, 20880);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static interface UserService{
		
		public String findUser(String userCode, int id);
		
	}
	
	public static class UserServiceImpl implements UserService{

		@Override
		public String findUser(String userCode, int id) {
			return "I'm name of " + userCode + ":" + id;
		}
		
	}

}
