package lam.study.sample;

import lam.study.StudyGson;
import lam.study.sample.dto.*;
import lam.study.sample.mapper.*;
import lam.study.sample.model.*;

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

		System.out.println(StudyGson.GSON.toJson(car));
		
		CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);
		
		System.out.println(StudyGson.GSON.toJson(carDto));

		carDto = CarMapper.INSTANCE.carToCarDtoSpecial(car);

		System.out.println(StudyGson.GSON.toJson(carDto));

		Person person = new Person();
		person.setId(1);
		person.setName("sky");
		person.setAge(100L);
		System.out.println("Person:" + StudyGson.GSON.toJson(person));

		PersonDto personDto = PersonMapper.INSTANCE.personToPersonDto(person);

		System.out.println("PersonDto:" + StudyGson.GSON.toJson(personDto));

		PersonCarDto personCarDto = PersonCarMapper.INSTANCE.toPersonCarDto(person, car);

		System.out.println("PersonCarDto:" + StudyGson.GSON.toJson(personCarDto));

		personCarDto = PersonCarMapper.INSTANCE.toPersonCarDto(car, 2, "lam");

		System.out.println("PersonCarDto:" + StudyGson.GSON.toJson(personCarDto));

		Car car0 = new Car();
		car0.setMake("car0 make");
		car0.setNumberOfSeats(1);
		car0.setType(CarType.SEAT);

		CarDto carDto1 = new CarDto();
		carDto1.setMake("carDto0 make");
		carDto1.setSeatCount(2);
		carDto1.setType("type");

		CarMapper.INSTANCE.updateCarDtoFromCar(car0, carDto1);
		System.out.println("carDto1:" + StudyGson.GSON.toJson(carDto1));

	}

}
