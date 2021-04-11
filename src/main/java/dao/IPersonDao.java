package dao;

import model.Person;

import java.time.LocalDate;
import java.util.List;

public interface IPersonDao {
    List<Person> getAll();

    Person getById(int id);

    List<Person> getByFirstName(String firstName);
    List<Person> getByLastName(String lastName);
    List<Person> getByBirthDateBetween(LocalDate from, LocalDate to);

    int addPerson(Person person);
    int deletePerson(int personId);
    int updatePerson(int personId, Person person);

}
