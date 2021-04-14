package dao;

import model.Person;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonDao implements IPersonDao {

    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/jdbsapp?useTimezone=true&serverTimezone=UTC";

    private int createId() {

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
            Class.forName("com.mysql.cj.jdbc.Driver"); // podaje się gdy jest więcej sterowników baz danych
            connection = DriverManager.getConnection(url, "root", "root");
        } catch (SQLException | ClassNotFoundException throwables) {
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
                Person person = getPersonFromResultSet(resultSet);
                personList.add(person);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        closeConnection();
        return personList;
    }

    private Person getPersonFromResultSet(ResultSet resultSet) throws SQLException {
        Person person = new Person();
        person.setPersonId(resultSet.getInt("person_id"));
        person.setFirstName(resultSet.getString("first_name"));
        person.setLastName(resultSet.getString("last_name"));
        Date birthDate = resultSet.getDate("birth_date");
        if (birthDate != null) {
            person.setBirthDate(birthDate.toLocalDate());
        }
        return person;
    }

    @Override
    public Person getById(int id) {
        String query = "select * from persons where person_id = ?";
        Person person = new Person();
        openConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                person = getPersonFromResultSet(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        closeConnection();
        return person;
    }

    @Override
    public List<Person> getByFirstName(String firstName) {
        // działanie na procedurze zapisanej w mysql
        String query = "call get_by_first_name(?);";
        List<Person> persons = new ArrayList<>();
        openConnection();
        try {
            CallableStatement statement = connection.prepareCall(query);
            statement.setString(1, firstName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                persons.add(getPersonFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return persons;
    }

    @Override
    public List<Person> getByLastName(String lastName) {
        // todo dokończyć samemu
        return null;
    }

    @Override
    public List<Person> getByBirthDateBetween(LocalDate from, LocalDate to) {

        return getAll().stream()
                .filter(p -> p.getBirthDate() != null && p.getBirthDate().isAfter(from) && p.getBirthDate().isBefore(to))
                .collect(Collectors.toList());
    }

    //    nie działa na metodzie executeUpdate
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

        return update(insert);
    }

    @Override
    public int deletePerson(int personId) {
        String delete = String.format("delete from persons where person_id = %d", personId);
        return update(delete);
    }


    @Override
    public int updatePerson(int personId, Person person) {
        String update = String.format("update persons set first_name = '%s', last_name = '%s', birth_date = '%s'" +
                " where person_id = %d", person.getFirstName(), person.getLastName(), person.getBirthDate(), personId);

        return update(update);
    }

    @Override
    public void checkExecute() {
        openConnection();
        String query = "call pm()";

        try {
            Statement preparedStatement = connection.createStatement();
            boolean isResult = preparedStatement.execute(query);
            while (isResult) {
                ResultSet resultSet = preparedStatement.getResultSet();
                while (resultSet.next()) {
                    System.out.println(resultSet.getObject(1) + " | " + resultSet.getObject(2));
                }
                isResult = preparedStatement.getMoreResults();
                System.out.println("===============================================");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        closeConnection();
    }

    private int update(String update) {
        openConnection();

        int result = 0;
        try {
            Statement statement = connection.createStatement();
            result = statement.executeUpdate(update);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        closeConnection();
        return result;
    }
}
