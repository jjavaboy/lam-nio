import lam.rpcframework.ReferFramework;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月11日
* @version 1.0
*/
public class RpcFrameworkReferTest {

	public static void main(String[] args) {
		try {
			RpcFrameworkServiceTest.UserService userService = 
					new ReferFramework().refer(RpcFrameworkServiceTest.UserService.class, "192.168.56.1", 20880);
			String user = userService.findUser("sky", 2);
			System.out.println(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
