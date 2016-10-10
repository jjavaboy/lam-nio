package lam.netty.http.json.model;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月9日
* @versio 1.0
*/
public class Address {
	
	private String street;
	private String area;
	private String city;
	private String state;
	private String country;
	private String postCode;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{")
		.append("street:").append(street)
		.append(",area:").append(area)
		.append(",city:").append(city)
		.append(",state:").append(state)
		.append(",country:").append(country)
		.append(",postCode:").append(postCode)
		.append("}");
		return sb.toString();
	}
	
	public Address(){}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
}
