package com.example.laboras.control;

import com.example.laboras.ds.Folder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.laboras.control.DbUtils.connectToDb;
import static com.example.laboras.control.DbUtils.disconnectFromDb;

public class FolderDbUtils {

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

}
