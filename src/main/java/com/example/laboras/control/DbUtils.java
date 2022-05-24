package com.example.laboras.control;

import com.example.laboras.ds.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    public static void deleteFolderFromDb(int folderId) {
        Connection connection = connectToDb();
        String sql = "DELETE FROM folders WHERE id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, folderId);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "DELETE FROM files WHERE parent_folder = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, folderId);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnectFromDb(connection, null);
    }

    public static void deleteFileFromDb(int fileId) {
        Connection connection = connectToDb();
        String sql = "DELETE FROM files WHERE id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, fileId);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnectFromDb(connection, null);
    }

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

        sql = "DELETE FROM user_course WHERE user_course.user_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnectFromDb(connection, null);
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

    public static void addFolder(String title, int parentCourseId) {
        try {
            Connection connection = connectToDb();
            String sql = "INSERT INTO folders (`title`, `parent_course`, `date_created`, `date_updated`) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, parentCourseId);
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.execute();
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addFile(String title, int parentFolderId) {
        try {
            Connection connection = connectToDb();
            String sql = "INSERT INTO files (`title`, `parent_folder`, `date_created`, `date_updated`) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, parentFolderId);
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.execute();
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            DbUtils.disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            DbUtils.disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
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

    public static ArrayList<Folder> getFoldersByCourseId(int courseId) {
        Connection connection = connectToDb();
        ArrayList<Folder> folders = new ArrayList<>();
        String sql = "SELECT * FROM folders WHERE folders.parent_course = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
                folders.add(new Folder(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDate(4), rs.getDate(5)));
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return folders;
    }

    public static ArrayList<File> getFilesByFolderId(int folderId) {
        Connection connection = connectToDb();
        ArrayList<File> files = new ArrayList<>();
        String sql = "SELECT * FROM files WHERE files.parent_folder = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, folderId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
                files.add(new File(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDate(4), rs.getDate(5)));
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
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

    public static Folder getFolderInfo(int id) {
        Connection connection = connectToDb();
        String sql = "SELECT * FROM folders WHERE folders.id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            Folder folder = new Folder(rs.getInt(1), rs.getString(2), rs.getInt(3),  rs.getDate(4), rs.getDate(5));
            disconnectFromDb(connection, preparedStatement);
            return folder;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getFolderFilesCount(int id) {
        int count=0;
        Connection connection = connectToDb();
        String sql = "SELECT COUNT(id) FROM files WHERE files.parent_folder = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            count=rs.getInt(1);
            disconnectFromDb(connection, preparedStatement);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static File getFileInfo(int id) {
        Connection connection = connectToDb();
        String sql = "SELECT * FROM files WHERE files.id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            File file = new File(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDate(4), rs.getDate(5));
            disconnectFromDb(connection, preparedStatement);
            return file;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getFileParentCourse(int parentFolderId) {
        int id=0;
        Connection connection = connectToDb();
        String sql = "SELECT * FROM folders WHERE folders.id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, parentFolderId);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            id = rs.getInt(3);
            disconnectFromDb(connection, preparedStatement);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
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
