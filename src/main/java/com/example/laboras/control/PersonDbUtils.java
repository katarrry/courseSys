package com.example.laboras.control;

import com.example.laboras.ds.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.laboras.control.DbUtils.connectToDb;
import static com.example.laboras.control.DbUtils.disconnectFromDb;

public class PersonDbUtils {

    public static void addPerson(String name, String surname, String login, String psw, String email) {
        try {
            Connection connection = connectToDb();
            String sql = "INSERT INTO users(`name`, `surname`, `login`, `psw`, `email`, `type`) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, psw);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, "P");
            preparedStatement.execute();
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Person> getAllPerson() {
        Connection connection = connectToDb();
        ArrayList<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE users.type = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "P");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
                people.add(new Person(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    public static ArrayList<Person> getCourseStudents(int courseId) {
        Connection connection = connectToDb();
        ArrayList<Person> students = new ArrayList<>();
        String sql = "SELECT users.id, users.name, users.surname FROM users,user_course,courses WHERE courses.id = user_course.course_id AND user_course.user_id = users.id AND courses.id = ? AND user_course.edit = 0 AND users.title IS NULL";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
                students.add(new Person(rs.getInt(1), rs.getString(2), rs.getString(3)));
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static ArrayList<Person> getCourseEditorsP(int courseId) {
        Connection connection = connectToDb();
        ArrayList<Person> people = new ArrayList<>();
        String sql = "SELECT users.id, users.name, users.surname FROM users,user_course,courses WHERE courses.id = user_course.course_id AND user_course.user_id = users.id AND courses.id = ? AND user_course.edit = 1 AND users.title IS NULL";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
                people.add(new Person(rs.getInt(1), rs.getString(2), rs.getString(3)));
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    public static Person getPersonInfo(int id) {
        Connection connection = connectToDb();
        String sql = "SELECT * FROM users WHERE users.id = ? ";
        boolean isAdmin=false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            if (rs.getString(8)=="A") isAdmin=true;
            Person person = new Person(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6), isAdmin);
            disconnectFromDb(connection, preparedStatement);
            return person;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
