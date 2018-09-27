package lam.study.sample.model;

import lam.study.sample.CarType;

import java.util.Date;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年9月8日
* @versio 1.0
*/
public class Car {
	
    private String make;
    private int numberOfSeats;
    private CarType type;
    private Date createTime;
    
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public int getNumberOfSeats() {
		return numberOfSeats;
	}
	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}
	public CarType getType() {
		return type;
	}
	public void setType(CarType type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
