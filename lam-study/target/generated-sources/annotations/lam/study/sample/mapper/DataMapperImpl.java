package lam.study.sample.mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import lam.study.sample.dto.CarDto;
import lam.study.sample.model.Car;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2018-11-26T19:35:31+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_111 (Oracle Corporation)"
)
public class DataMapperImpl implements DataMapper {

    @Override
    public CarDto dateToString(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDto carDto = new CarDto();

        if ( car.getCreateTime() != null ) {
            carDto.setDate( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss SSS" ).format( car.getCreateTime() ) );
        }
        carDto.setMake( car.getMake() );
        if ( car.getType() != null ) {
            carDto.setType( car.getType().name() );
        }

        return carDto;
    }

    @Override
    public List<String> dateListToStringList(List<Date> dates) {
        if ( dates == null ) {
            return null;
        }

        List<String> list = new ArrayList<String>( dates.size() );
        for ( Date date : dates ) {
            list.add( new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss,sss" ).format( date ) );
        }

        return list;
    }
}
