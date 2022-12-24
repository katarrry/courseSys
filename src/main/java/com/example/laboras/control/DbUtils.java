package com.example.laboras.control;

import com.example.laboras.ds.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.example.laboras.control.CourseDbUtils.deleteCourseFromDb;

public class DbUtils {
    public static Connection connectToDb() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String DB_URL = "jdbc:mysql://localhost:3306/course";
            String USER = "root";
            String PASS = "";
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException t) {
            t.printStackTrace();
        }
        return conn;
    }

    public static void disconnectFromDb(Connection connection, Statement statement) {

        try {
            if (connection != null && statement != null) {
                connection.close();
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTableString(String dbTable, String dbColName, String value, int id) {
        Connection connection = connectToDb();
        String sql = "UPDATE " + dbTable + " SET " + dbColName + " = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTableDate(String dbTable, String dbColName, LocalDate value, int id) {
        Connection connection = connectToDb();
        String sql = "UPDATE " + dbTable + " SET " + dbColName + " = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(value));
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
