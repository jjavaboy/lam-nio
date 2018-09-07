package lam.study.sample;

import lam.study.StudyGson;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年9月8日
* @versio 1.0
*/
public class SampleTest {
	
	public static void main(String[] args) {
		Car car = new Car();
		car.setMake("makename");
		car.setNumberOfSeats(2);
		car.setType(CarType.SEAT);
		
		CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);
		
		System.out.println(StudyGson.GSON.toJson(carDto));
	}

}
