package com.example.laboras.control;

import com.example.laboras.ds.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.laboras.control.DbUtils.connectToDb;
import static com.example.laboras.control.DbUtils.disconnectFromDb;

public class CompanyDbUtils {

    public static void addCompany(String name, String surname, String login, String psw, String email, String title) {
        try {
            Connection connection = connectToDb();
            String sql = "INSERT INTO users(`name`, `surname`, `login`, `psw`, `email`, `title`, `type`) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, psw);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, title);
            preparedStatement.setString(7, "C");
            preparedStatement.execute();
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Company> getAllCompanies() {
        Connection connection = connectToDb();
        ArrayList<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE users.type = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "C");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
                companies.add(new Company(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    public static ArrayList<Company> getCourseEditorsC(int courseId) {
        Connection connection = connectToDb();
        ArrayList<Company> companies = new ArrayList<>();
        String sql = "SELECT users.id, users.title FROM users,user_course,courses WHERE courses.id = user_course.course_id AND user_course.user_id = users.id AND courses.id = ? AND user_course.edit = 1 AND users.title IS NOT NULL";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
                companies.add(new Company(rs.getInt(1), rs.getString(2)));
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    public static Company getCompanyInfo(int id) {
        Connection connection = connectToDb();
        String sql = "SELECT * FROM users WHERE users.id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            Company company = new Company(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6), rs.getString(7));
            disconnectFromDb(connection, preparedStatement);
            return company;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
