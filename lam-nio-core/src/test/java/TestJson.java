import com.google.gson.Gson;

import lam.netty.http.json.factory.OrderFactory;
import lam.netty.http.json.model.Order;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月9日
* @versio 1.0
*/
public class TestJson {

	public static void main(String[] args) {
		Order order = OrderFactory.create(1);
		String str = new Gson().toJson(order);
		System.out.println(str);
		
		Order order1 = new Gson().fromJson(str, Order.class);
		System.out.println(new Gson().toJson(order1));
	}

}
