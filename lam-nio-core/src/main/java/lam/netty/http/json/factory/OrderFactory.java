package lam.netty.http.json.factory;

import lam.netty.http.json.model.Address;
import lam.netty.http.json.model.Customer;
import lam.netty.http.json.model.Order;
import lam.netty.http.json.model.Shipping;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月9日
* @versio 1.0
*/
public class OrderFactory {
	
	public static Order create(long orderNo){
		Address address = new Address();
		address.setArea("天河区");
		address.setCity("广州市");
		address.setPostCode("510000");
		address.setState("广东省");
		address.setCountry("中国");
		address.setStreet("中山大道");
		
		Customer customer = new Customer();
		customer.setIdno("123456789");
		customer.setFirstName("lin");
		customer.setLastName("anmiao");
		
		Order order = new Order();
		order.setAddress(address);
		order.setCustomer(customer);
		order.setOrderNo(orderNo);
		order.setShipping(Shipping.INTERNATIONAL_EXPRESS);
		order.setTotal(999.99f);
		
		return order;
	}

}
