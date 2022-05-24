package com.example.laboras;

import com.example.laboras.control.Constants;
import com.example.laboras.control.DbUtils;
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

public class UpdateCourseWindow {
    @FXML
    public TextField titleF;
    @FXML
    public TextArea descF;
    @FXML
    public DatePicker startDateF;
    @FXML
    public DatePicker endDateF;

    private String login;


    public void updateCourseToDb(ActionEvent actionEvent) throws IOException {
        boolean isDateOk = true;
        if (titleF.getText() != "") {
            DbUtils.updateTableString("courses", "title", titleF.getText(), Constants.courseId);
        }
        if (descF.getText() != "") {
            DbUtils.updateTableString("courses", "description", descF.getText(), Constants.courseId);
        }
        try {
            startDateF.getConverter().fromString(startDateF.getEditor().getText());
        } catch (DateTimeParseException e) {
            isDateOk = false;
        }
        if (isDateOk && endDateF.getValue() != null && startDateF.getValue() != null) {
            if (startDateF.getValue().compareTo(endDateF.getValue()) > 0) {
                isDateOk = false;
            }
        } else isDateOk = false;
        if (isDateOk) {
            DbUtils.updateTableDate("courses", "start_date", startDateF.getValue(), Constants.courseId);
            isDateOk = true;
        }
        try {
            endDateF.getConverter().fromString(endDateF.getEditor().getText());
        } catch (DateTimeParseException e) {
            isDateOk = false;
        }
        if (isDateOk) {
            DbUtils.updateTableDate("courses", "end_date", endDateF.getValue(), Constants.courseId);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("course-window.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) titleF.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
