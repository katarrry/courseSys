package com.example.laboras;

import com.example.laboras.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SignupWindow implements Initializable {
    @FXML
    public RadioButton radioP;
    @FXML
    public ToggleGroup userType;
    @FXML
    public RadioButton radioC;
    @FXML
    public TextField userNameF;
    @FXML
    public TextField userSurnameF;
    @FXML
    public TextField userLoginF;
    @FXML
    public PasswordField userPswF;
    @FXML
    public TextField companyTitleF;
    @FXML
    public Label titleLabel;
    @FXML
    public RadioButton radioA;
    @FXML
    public TextField userEmailF;

    public void returnToLogin(ActionEvent actionEvent) throws IOException {
        returnToPrevious();
    }

    public void createUser(ActionEvent actionEvent) throws IOException {
        if (userLoginF.getText() == "" || userPswF.getText() == "" || userNameF.getText() == "" || userSurnameF.getText() == "" || (radioC.isSelected() && companyTitleF.getText() == "")) {
            LoginWindow.alertMessage("Please fill all the text fields");
        }
        else if (radioC.isSelected()) {
            CompanyDbUtils.addCompany(userNameF.getText(), userSurnameF.getText(), userLoginF.getText(), userPswF.getText(), userEmailF.getText(), companyTitleF.getText());
            LoginWindow.alertMessage("Company created successfully.");
            returnToPrevious();
        }
        else if (radioA.isSelected()) {
            UserDbUtils.addAdmin(userNameF.getText(), userSurnameF.getText(), userLoginF.getText(), userPswF.getText(), userEmailF.getText());
            LoginWindow.alertMessage("Admin created successfully.");
            returnToPrevious();
        }
        else  {
            PersonDbUtils.addPerson(userNameF.getText(), userSurnameF.getText(), userLoginF.getText(), userPswF.getText(), userEmailF.getText());
            LoginWindow.alertMessage("User created successfully.");
            returnToPrevious();
        }

    }

    public void disableField(ActionEvent actionEvent) {
        if (radioP.isSelected() || radioA.isSelected()) {
            companyTitleF.setVisible(false);
            titleLabel.setVisible(false);
        } else {
            companyTitleF.setVisible(true);
            titleLabel.setVisible(true);
        }
    }

    private void returnToPrevious() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("login-window.fxml"));
        ReturnHandler.returnMethod(fxmlLoader,(Stage)companyTitleF.getScene().getWindow());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        companyTitleF.setVisible(false);
        titleLabel.setVisible(false);
    }

}
