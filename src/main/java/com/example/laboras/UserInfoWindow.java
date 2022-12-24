package com.example.laboras;

import com.example.laboras.control.*;
import com.example.laboras.ds.Company;
import com.example.laboras.ds.Folder;
import com.example.laboras.ds.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserInfoWindow implements Initializable {
    @FXML
    public TextField name;
    @FXML
    public TextField surname;
    @FXML
    public TextField email;
    @FXML
    public TextField title;
    @FXML
    public TextField login;
    @FXML
    public TextField psw;
    @FXML
    public Label id;
    @FXML
    public Button save;
    @FXML
    public Button delete;
    @FXML
    public Button back;

    String userType="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userType = UserDbUtils.getUserType(Constants.listUserId);
        if (!userType.equals("C")) {
            title.setVisible(false);
            Person person = PersonDbUtils.getPersonInfo(Constants.listUserId);
            if(person != null) {
                id.setText("User ID: " + person.getId());
                name.setText(person.getName());
                surname.setText(person.getSurname());
                email.setText(person.getEmail());
                login.setText(person.getLogin());
            }
        }
        else {
            Company company = CompanyDbUtils.getCompanyInfo(Constants.listUserId);
            if(company != null) {
                id.setText("User ID: " + company.getId());
                name.setText(company.getName());
                surname.setText(company.getSurname());
                email.setText(company.getEmail());
                login.setText(company.getLogin());
                title.setText(company.getTitle());
            }
        }
    }

    public void updateUser(ActionEvent actionEvent) {
        if (name.getText()=="" || surname.getText() == "" || email.getText() == "" || login.getText() =="" || psw.getText() == "" || (userType=="C" && title.getText() =="")) {
            LoginWindow.alertMessage("Please fill all the text fields");
        }
        else {
            DbUtils.updateTableString("users", "name", name.getText(), Constants.listUserId);
            DbUtils.updateTableString("users", "surname", surname.getText(), Constants.listUserId);
            DbUtils.updateTableString("users", "email", email.getText(), Constants.listUserId);
            DbUtils.updateTableString("users", "login", login.getText(), Constants.listUserId);
            DbUtils.updateTableString("users", "psw", psw.getText(), Constants.listUserId);
            if (userType.equals("C"))
                DbUtils.updateTableString("users", "title", title.getText(), Constants.listUserId);
            LoginWindow.alertMessage("User updated");
        }
    }

    public void returnToPrevious () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("course-window.fxml"));
        ReturnHandler.returnMethod(fxmlLoader,(Stage)title.getScene().getWindow());
    }

    public void deleteUser(ActionEvent actionEvent) {
        UserDbUtils.deleteUserFromDb(Constants.listUserId);
        LoginWindow.alertMessage("User Deleted");
        try {
            returnToPrevious();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void returnToCourse(ActionEvent actionEvent) throws IOException {
        returnToPrevious();
    }

}
