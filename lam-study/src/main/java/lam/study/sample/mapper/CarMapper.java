package lam.study.sample.mapper;

import lam.study.sample.dto.*;
import lam.study.sample.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年9月8日
* @versio 1.0
*/
@Mapper
public interface CarMapper {
	
	CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);
	
	CarDto carToCarDto(Car car);

	@Mappings({
			@Mapping(source = "numberOfSeats", target = "seatCount")
	})
	CarDto carToCarDtoSpecial(Car car);

	/**
	 *  update field of CarDto carDto from field of Car car
	 */
	void updateCarDtoFromCar(Car car, @MappingTarget CarDto carDto);
}
