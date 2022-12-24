package com.example.laboras.control;

import com.example.laboras.ds.File;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.laboras.control.DbUtils.connectToDb;
import static com.example.laboras.control.DbUtils.disconnectFromDb;

public class FileDbUtils {

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

}
