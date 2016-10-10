package lam.netty.http.json.model;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月9日
* @versio 1.0
*/
public class Customer {
	
	private String idno;
	
	private String firstName;
	
	private String lastName;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{")
		.append("idno:").append(idno)
		.append(",firstName:").append(firstName)
		.append(",lastName:").append(lastName)
		.append("}");
		return sb.toString();
	}
	
	public Customer(){}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
