package dao;

import model.Person;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonDao implements IPersonDao {

    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/jdbsapp?useTimezone=true&serverTimezone=UTC";

    private int createId() {

        // todo nie działa
        String query = "select max(person_id) from persons";
        int res = 0;
        openConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                res = result.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        closeConnection();
        return ++res;
    }

    private void openConnection() {
        try {
            connection = DriverManager.getConnection(url, "root", "root");
            System.out.println("connected");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public List<Person> getAll() {

        List<Person> personList = new ArrayList<>();
        openConnection();
        try {
            String query = "select * from persons";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Person person = new Person();
                person.setPersonId(resultSet.getInt("person_id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                Date birthDate = resultSet.getDate("birth_date");
                if (birthDate != null) {
                    person.setBirthDate(birthDate.toLocalDate());
                }
                personList.add(person);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        closeConnection();
        return personList;
    }

    @Override
    public Person getById(int id) {
        return null;
    }

    @Override
    public List<Person> getByFirstName(String firstName) {
        return null;
    }

    @Override
    public List<Person> getByLastName(String lastName) {
        return null;
    }

    @Override
    public List<Person> getByBirthDateBetween(LocalDate from, LocalDate to) {
        return null;
    }

//    @Override
//    public int addPerson(Person person) {
//        String insert = "insert into persons values(?, ?, ?)";
//
//        openConnection();
//
//        int result = 0;
//        try {
//            PreparedStatement statement = connection.prepareStatement(insert);
//            statement.setInt(1, person.getPersonId());
//            statement.setString(2, person.getFirstName());
//            statement.setString(3, person.getLastName());
//
//            result = statement.executeUpdate(insert);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        closeConnection();
//        return result;
//    }

    @Override
    public int addPerson(Person person) {
//        String insert = "insert into persons values(" + person.getPersonId()
//                + ", '" + person.getFirstName() + "', '" + person.getLastName() + "');";

        String insert = String.format("insert into persons values(%d, '%s', '%s', '%s')",
                createId(), person.getFirstName(), person.getLastName(),
                person.getBirthDateAsString());

        openConnection();

        int result = 0;
        try {
            Statement statement = connection.createStatement();
            result = statement.executeUpdate(insert);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        closeConnection();
        return result;
    }

    @Override
    public int deletePerson(int personId) {
        String delete = String.format("delete from persons where person_id = %d", personId);

        openConnection();

        int result = 0;
        try {
            Statement statement = connection.createStatement();
            result = statement.executeUpdate(delete);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        closeConnection();
        System.out.println("removed " + result + " records");
        return result;
    }

    @Override
    public int updatePerson(int personId, Person person) {
        return 0;
    }
}
