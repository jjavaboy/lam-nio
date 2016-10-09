package lam.netty.http.json.model;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月9日
* @versio 1.0
*/
public class Order {
	
	private Long orderNo;
	
	private Customer customer;
	
	private Address address;
	
	private Shipping shipping;
	
	private Float total;

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}
	
}
