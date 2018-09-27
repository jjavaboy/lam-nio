package lam.study.sample.dto;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年9月8日
* @versio 1.0
*/
public class CarDto {
	
	private String make;
    private int seatCount;
    private String type;

    private String date;
    
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public int getSeatCount() {
		return seatCount;
	}
	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
