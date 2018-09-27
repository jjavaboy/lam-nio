package lam.study.sample.mapper;

import lam.study.sample.model.*;
import lam.study.sample.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @description: PersonCarMapper
 * @author: linanmiao
 * @date: 2018/9/24 21:06
 * @version: 1.0
 */
@Mapper
public interface PersonCarMapper {

    PersonCarMapper INSTANCE = Mappers.getMapper(PersonCarMapper.class);

    @Mappings({
            @Mapping(source = "car.make", target = "make"),
            @Mapping(source = "car.numberOfSeats", target = "seatCount"),
            @Mapping(source = "car.type", target = "type"),
            @Mapping(source = "person.id", target = "id"),
            @Mapping(source = "person.name", target = "name"),
            @Mapping(source = "person.age", target = "age")
    })
    PersonCarDto toPersonCarDto(Person person, Car car);

    @Mappings({
            @Mapping(source = "car.make", target = "make"),
            @Mapping(source = "car.numberOfSeats", target = "seatCount"),
            @Mapping(source = "car.type", target = "type"),
            @Mapping(source = "key", target = "id"),
            @Mapping(source = "value", target = "name")
    })
    PersonCarDto toPersonCarDto(Car car, Integer key, String value);

}
