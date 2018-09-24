package lam.study.sample;

import lam.study.StudyGson;
import lam.study.sample.dto.PersonCarDto;
import lam.study.sample.dto.PersonDto;
import lam.study.sample.mapper.CarMapper;
import lam.study.sample.mapper.PersonCarMapper;
import lam.study.sample.mapper.PersonMapper;
import lam.study.sample.model.Person;

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
		lam.study.sample.Car car = new lam.study.sample.Car();
		car.setMake("makename");
		car.setNumberOfSeats(2);
		car.setType(CarType.SEAT);

		System.out.println(StudyGson.GSON.toJson(car));
		
		lam.study.sample.CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);
		
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
	}

}
