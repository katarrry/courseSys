package com.example.laboras;

import com.example.laboras.control.Constants;
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

public class LoginWindow {
    @FXML
    public TextField loginF;
    @FXML
    public PasswordField pswF;

    public void validateLogin(ActionEvent actionEvent) throws IOException {
        int id = DbUtils.validateByCredentials(loginF.getText(), pswF.getText());
        if (id!=0) {
            Constants.userId = id;
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

    public static void alertMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Message text:");
        alert.setContentText(s);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    public void createNewUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("signup-window.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) loginF.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
