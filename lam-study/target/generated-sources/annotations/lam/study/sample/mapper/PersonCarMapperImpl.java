package lam.study.sample.mapper;

import javax.annotation.Generated;
import lam.study.sample.dto.PersonCarDto;
import lam.study.sample.model.Car;
import lam.study.sample.model.Person;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2018-11-26T19:35:31+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_111 (Oracle Corporation)"
)
public class PersonCarMapperImpl implements PersonCarMapper {

    @Override
    public PersonCarDto toPersonCarDto(Person person, Car car) {
        if ( person == null && car == null ) {
            return null;
        }

        PersonCarDto personCarDto = new PersonCarDto();

        if ( person != null ) {
            personCarDto.setName( person.getName() );
            personCarDto.setId( person.getId() );
            personCarDto.setAge( person.getAge() );
        }
        if ( car != null ) {
            if ( car.getType() != null ) {
                personCarDto.setType( car.getType().name() );
            }
            personCarDto.setMake( car.getMake() );
            personCarDto.setSeatCount( car.getNumberOfSeats() );
        }

        return personCarDto;
    }

    @Override
    public PersonCarDto toPersonCarDto(Car car, Integer key, String value) {
        if ( car == null && key == null && value == null ) {
            return null;
        }

        PersonCarDto personCarDto = new PersonCarDto();

        if ( car != null ) {
            if ( car.getType() != null ) {
                personCarDto.setType( car.getType().name() );
            }
            personCarDto.setMake( car.getMake() );
            personCarDto.setSeatCount( car.getNumberOfSeats() );
        }
        if ( key != null ) {
            personCarDto.setId( key );
        }
        if ( value != null ) {
            personCarDto.setName( value );
        }

        return personCarDto;
    }
}
