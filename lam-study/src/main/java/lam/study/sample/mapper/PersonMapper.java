package lam.study.sample.mapper;

import lam.study.StudyGson;
import lam.study.sample.dto.PersonDto;
import lam.study.sample.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description: PersonMapper
 * @author: linanmiao
 * @date: 2018/9/24 20:33
 * @version: 1.0
 */
@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    //public PersonDto personToPersonDto(Person person);

    default PersonDto personToPersonDto(Person person) {
        System.out.println("[personToPersonDto] Person:" + StudyGson.GSON.toJson(person));
        if (person == null) {
            return null;
        }
        PersonDto personDto = new PersonDto();
        personDto.setKey(person.getId());
        personDto.setValue(person.getName());
        personDto.setAges(person.getAge());
        return personDto;
    }

}
