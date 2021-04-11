
import dao.IPersonDao;
import dao.PersonDao;
import model.Person;

import java.sql.*;
import java.time.LocalDate;

public class App {
    public static void main(String[] args) {

        Person person = new Person("Jarek", "Marek", LocalDate.now());
//        person.setPersonId();

        IPersonDao personDao = new PersonDao();

//        personDao.addPerson(person);

        personDao.deletePerson(8);
        System.out.println(personDao.getAll());


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