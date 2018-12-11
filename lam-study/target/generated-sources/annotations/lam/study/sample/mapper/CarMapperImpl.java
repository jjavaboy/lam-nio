package lam.study.sample.mapper;

import javax.annotation.Generated;
import lam.study.sample.dto.CarDto;
import lam.study.sample.model.Car;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2018-11-26T19:35:31+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_111 (Oracle Corporation)"
)
public class CarMapperImpl implements CarMapper {

    @Override
    public CarDto carToCarDto(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDto carDto = new CarDto();

        carDto.setMake( car.getMake() );
        if ( car.getType() != null ) {
            carDto.setType( car.getType().name() );
        }

        return carDto;
    }

    @Override
    public CarDto carToCarDtoSpecial(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDto carDto = new CarDto();

        carDto.setSeatCount( car.getNumberOfSeats() );
        carDto.setMake( car.getMake() );
        if ( car.getType() != null ) {
            carDto.setType( car.getType().name() );
        }

        return carDto;
    }

    @Override
    public void updateCarDtoFromCar(Car car, CarDto carDto) {
        if ( car == null ) {
            return;
        }

        carDto.setMake( car.getMake() );
        if ( car.getType() != null ) {
            carDto.setType( car.getType().name() );
        }
    }
}
