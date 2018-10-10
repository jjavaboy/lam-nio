package lam.study.sample.mapper;

import lam.study.sample.dto.CarDto;
import lam.study.sample.model.Car;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;

/**
 * @description: DataMapper
 * @author: linanmiao
 * @date: 2018/9/27 23:46
 * @version: 1.0
 */
@Mapper
public interface DataMapper {

    DataMapper INSTANCE = Mappers.getMapper(DataMapper.class);

    @Mapping(source = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss SSS", target = "date")
    CarDto dateToString(Car car);

    @IterableMapping(dateFormat = "yyyy/MM/dd HH:mm:ss,sss")
    List<String> dateListToStringList(List<Date> dates);
}
