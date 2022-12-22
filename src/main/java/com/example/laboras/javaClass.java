package com.example.laboras;

import com.example.laboras.control.DbUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class javaClass {
    @FXML
    public TextField loginFf;
    @FXML
    public PasswordField pswFf;

    public void validateLogin(ActionEvent actionEvent) throws IOException {
        int id = validateByCredentials(loginFf.getText(), pswFf.getText()), userId;
        if (id!=0) {
            userId = id;
            FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("course-window.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        else {
            alertMessage("Wrong input data, no such user found");
        }
    }

    public static int validateByCredentials(String login, String psw) {
        Connection connection = DbUtils.connectToDb();
        int id=0;
        try {
            String selectUser = "SELECT * FROM users AS u WHERE u.login = ? AND u.psw = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectUser);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, psw);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
                DbUtils.disconnectFromDb(connection, preparedStatement);
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static void alertMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Message text:");
        alert.setContentText(s);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }


}
