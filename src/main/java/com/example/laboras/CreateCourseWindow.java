package com.example.laboras;

import com.example.laboras.control.Constants;
import com.example.laboras.control.CourseDbUtils;
import com.example.laboras.control.DbUtils;
import com.example.laboras.control.ReturnHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeParseException;

public class CreateCourseWindow {
    @FXML
    public TextField titleF;
    @FXML
    public TextArea descF;
    @FXML
    public DatePicker startDateF;
    @FXML
    public DatePicker endDateF;


    public void addCourseToDb(ActionEvent actionEvent) throws IOException {
        boolean isDateOk = true;
        try {
            startDateF.getConverter().fromString(startDateF.getEditor().getText());
            endDateF.getConverter().fromString(endDateF.getEditor().getText());
        } catch (DateTimeParseException e) {
            LoginWindow.alertMessage("Date is not valid");
            isDateOk = false;
        }

        if (isDateOk && titleF.getText() != "" && descF.getText() != "" && endDateF.getValue() != null && startDateF.getValue() != null) {
            if (startDateF.getValue().compareTo(endDateF.getValue()) <= 0) {
                CourseDbUtils.addCourse(titleF.getText(), descF.getText(), startDateF.getValue(), endDateF.getValue(), Constants.userId);
                FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("course-window.fxml"));
                ReturnHandler.returnMethod(fxmlLoader,(Stage)titleF.getScene().getWindow());
            } else {
                LoginWindow.alertMessage("Cannot create course! Start date is after the end date");
            }
        } else {
            LoginWindow.alertMessage("Cannot create course! Please fill all the text fields");
        }
    }

}
