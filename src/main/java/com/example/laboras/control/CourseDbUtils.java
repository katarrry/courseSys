package com.example.laboras.control;

import com.example.laboras.ds.Course;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.laboras.control.DbUtils.connectToDb;
import static com.example.laboras.control.DbUtils.disconnectFromDb;
import static com.example.laboras.control.FolderDbUtils.deleteFolderFromDb;

public class CourseDbUtils {

    public static void deleteCourseFromDb(int courseId) {
        Connection connection = connectToDb();
        String sql = "DELETE FROM user_course WHERE user_course.course_id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "DELETE FROM courses WHERE id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT * FROM folders WHERE parent_course = ? ";
        ArrayList<Integer> folders = new ArrayList<>();;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                folders.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Integer f : folders) {
            deleteFolderFromDb(f);
        }

        disconnectFromDb(connection, null);
    }

    public static void addCourse(String title, String desc, LocalDate start_date, LocalDate end_date, int id) {
        try {
            Connection connection = connectToDb();
            String sql = "INSERT INTO courses (`title`, `description`, `start_date`, `end_date`, `creator`) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, desc);
            preparedStatement.setDate(3, Date.valueOf(start_date));
            preparedStatement.setDate(4, Date.valueOf(end_date));
            preparedStatement.setInt(5, id);
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int courseId = generatedKeys.getInt(1);
                    sql = "INSERT INTO user_course (`user_id`, `course_id`, `edit`) VALUES (?,?,?)";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, id);
                    preparedStatement.setInt(2, courseId);
                    preparedStatement.setInt(3, 0);
                    preparedStatement.execute();
                }
            }
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Course> getCoursesByUser(int id) {
        Connection connection = connectToDb();
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses,user_course,users WHERE courses.id = user_course.course_id AND user_course.user_id = users.id AND users.id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
                courses.add(new Course(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getDate(5), rs.getInt(6)));
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static ArrayList<Course> getAllCourses() {
        Connection connection = connectToDb();
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
                courses.add(new Course(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getDate(5), rs.getInt(6)));
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static boolean isCourseCreator(int userId, int courseId) {
        Connection connection = connectToDb();
        String sql = "SELECT * FROM courses WHERE courses.creator = ? AND courses.id = ? ";
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

    public static int getCourseCreator(int courseId) {
        int creatorId=0;
        Connection connection = connectToDb();
        String sql = "SELECT * FROM courses WHERE courses.id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                creatorId = rs.getInt(6);
                disconnectFromDb(connection, preparedStatement);
                return creatorId;
            }
            disconnectFromDb(connection, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return creatorId;
    }

    public static Course getCourseInfo(int id) {
        Connection connection = connectToDb();
        String sql = "SELECT * FROM courses WHERE courses.id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            Course course = new Course(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getDate(5), rs.getInt(6));
            disconnectFromDb(connection, preparedStatement);
            return course;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
