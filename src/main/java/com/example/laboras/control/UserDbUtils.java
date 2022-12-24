package com.example.laboras.control;

import com.example.laboras.ds.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.laboras.control.CourseDbUtils.deleteCourseFromDb;
import static com.example.laboras.control.DbUtils.connectToDb;
import static com.example.laboras.control.DbUtils.disconnectFromDb;

public class UserDbUtils {

    public static boolean deleteUserFromCourseDb(int userId, int courseId) {
        Connection connection = connectToDb();
        String sql = "SELECT * FROM user_course WHERE user_course.user_id = ? AND user_course.course_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, courseId);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                disconnectFromDb(connection, preparedStatement);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql1 = "DELETE FROM user_course WHERE user_course.user_id = ? AND user_course.course_id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql1);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnectFromDb(connection, null);
        return true;
    }

    public static void deleteUserFromDb(int id) {
        Connection connection = connectToDb();
        String sql = "DELETE FROM users where users.id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT * FROM courses WHERE courses.creator = ? ";
        ArrayList<Integer> courses = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                courses.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!courses.isEmpty()) {
            for (Integer c : courses)
                deleteCourseFromDb(c);
        }
    }

    public static void updateEditor(int userId, int courseId) {
        Connection connection = connectToDb();
        String sql = "UPDATE user_course SET user_course.edit = 0 WHERE user_id = ? AND course_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.execute();
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void grantEditorRights(int userId, int courseId) {
        Connection connection = connectToDb();
        String sql = "UPDATE user_course SET user_course.edit = 1 WHERE user_id = ? AND course_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.execute();
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int validateByCredentials(String login, String psw) {
        Connection connection = connectToDb();
        int id=0;
        try {
            String selectUser = "SELECT * FROM users AS u WHERE u.login = ? AND u.psw = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectUser);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, psw);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
                disconnectFromDb(connection, preparedStatement);
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static void addEditor(int courseId, int userId) {
        Connection connection = connectToDb();
        String sql = "SELECT * FROM user_course WHERE user_course.user_id = ? AND user_course.course_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, courseId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                sql = "UPDATE user_course SET edit = 1 WHERE user_id = ? AND course_id = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, courseId);
                preparedStatement.execute();
                DbUtils.disconnectFromDb(connection, preparedStatement);
                return;
            }
            else {
                String sql1 = "INSERT INTO user_course(`user_id`, `course_id`, `edit`) VALUES (?,?,?)";
                preparedStatement = connection.prepareStatement(sql1);
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, courseId);
                preparedStatement.setInt(3, 1);
                preparedStatement.execute();
                DbUtils.disconnectFromDb(connection, preparedStatement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAdmin(String name, String surname, String login, String psw, String email) {
        try {
            Connection connection = connectToDb();
            String sql = "INSERT INTO users(`name`, `surname`, `login`, `psw`, `email`, `type`) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, psw);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, "A");
            preparedStatement.execute();
            DbUtils.disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isModerator(int userId, int courseId) {
        Connection connection = connectToDb();
        String sql = "SELECT * FROM user_course WHERE user_course.user_id = ? AND user_course.course_id = ? AND user_course.edit = 1 ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, courseId);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                disconnectFromDb(connection, preparedStatement);
                return false;
            }
            disconnectFromDb(connection, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getUserType(int userId) {
        String type = "";
        Connection connection = connectToDb();
        String sql = "SELECT * FROM users WHERE users.id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                type = rs.getString(8);
                disconnectFromDb(connection, preparedStatement);
                return type;
            }
            disconnectFromDb(connection, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return type;
    }

    public static boolean enroll(int userId, int courseId) {
        Connection connection = connectToDb();
        if (!isStudentEnrolled(userId, courseId)) {
            String sql = "INSERT INTO user_course (`user_id`, `course_id`, `edit`) VALUES (?,?,?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, courseId);
                preparedStatement.setInt(3, 0);
                preparedStatement.execute();
                disconnectFromDb(connection, preparedStatement);
                return true;
            }
            catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static boolean isStudentEnrolled(int studentId, int courseId) {
        Connection connection = connectToDb();
        String sql = "SELECT * FROM courses WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                disconnectFromDb(connection, preparedStatement);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql1 = "SELECT * FROM user_course WHERE user_id = ? AND course_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql1);
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                disconnectFromDb(connection, preparedStatement);
                return false;
            }
            disconnectFromDb(connection, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
