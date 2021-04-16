
import dao.IPersonDao;
import dao.PersonDao;
import model.Person;

import java.sql.*;
import java.time.LocalDate;

public class App {
    public static void main(String[] args) {

        Person person = new Person("Iwan", "Iwański", LocalDate.of(1998, 02, 13));
//        person.setPersonId();

        IPersonDao personDao = new PersonDao();

//        personDao.addPerson(person);

//        personDao.deletePerson(8);

//        personDao.updatePerson(7, person);

        System.out.println(personDao.getAll());

        System.out.println("by id: " + personDao.getById(2));

        System.out.println(personDao.getByFirstName("Jan"));
        System.out.println("between: " + personDao.getByBirthDateBetween(
                LocalDate.of(1990, 01, 01),
                LocalDate.of(2000, 01, 01)
        ));

        personDao.checkExecute();

        System.out.println(personDao.getByLastName("Marek"));
    }

    private static void początek() {
        // jdbsapp - nazwa bazy
        // ?useTimezone=true&serverTimezone=UTC" - jeśli problem ze strefą czasową
        String url = "jdbc:mysql://localhost:3306/jdbsapp?useTimezone=true&serverTimezone=UTC";

        try {
            Connection connection = DriverManager.getConnection(url, "root", "root");
            System.out.println("connected");

            String query = "select * from persons";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " | " + resultSet.getString(2) + " | "
                        + resultSet.getString(3));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}