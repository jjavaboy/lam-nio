package lam.study.sample.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import lam.study.sample.dto.CarDto;
import lam.study.sample.model.Car;

/**
 * @author: linanmiao
 */
@Mapper(uses = DateMapper.class)
public interface CarWithUsesMapper {

    static CarWithUsesMapper INSTANCE = Mappers.getMapper(CarWithUsesMapper.class);

    @Mapping(source = "createTime", target = "date")
    CarDto toCarDto(Car car);

}
