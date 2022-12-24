package com.example.laboras;

import com.example.laboras.control.*;
import com.example.laboras.ds.File;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FileInfoWindow implements Initializable {
    @FXML
    public Label id;
    @FXML
    public Label title;
    @FXML
    public Label dateCreated;
    @FXML
    public Label dateUpdated;
    @FXML
    public Button saveTitle;
    @FXML
    public TextField newTitle;
    @FXML
    public Button deleteFile;
    @FXML
    public Button back;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = FileDbUtils.getFileInfo(Constants.fileId);
        if(file != null) {
            id.setText("ID: " + file.getId());
            title.setText("Title: " + file.getTitle());
            dateCreated.setText("Date created: " + file.getDateCreated());
            dateUpdated.setText("Date updated: " + file.getDateUpdated());
            int courseId = FolderDbUtils.getFileParentCourse(file.getParentFolder());
            if (!(CourseDbUtils.isCourseCreator(Constants.userId, courseId) || Constants.userType.equals("A"))) {
                newTitle.setVisible(false);
                saveTitle.setVisible(false);
                deleteFile.setVisible(false);
            }
        }
    }

    public void updateFileTitle(ActionEvent actionEvent) {
        if (!newTitle.getText().equals("")) {
            DbUtils.updateTableString("files", "title", newTitle.getText(), Constants.fileId);
            title.setText("Title: "+newTitle.getText());
            DbUtils.updateTableDate("files", "date_updated", LocalDate.now(), Constants.fileId);
            dateUpdated.setText("Date updated: "+LocalDate.now());
            LoginWindow.alertMessage("File Updated!");
        }
        else {
            LoginWindow.alertMessage("Please provide file title");
        }
    }

    public void deleteFile(ActionEvent actionEvent) {
        FileDbUtils.deleteFileFromDb(Constants.fileId);
        LoginWindow.alertMessage("File deleted");
        try {
            returnToPrevious();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void returnToCourse(ActionEvent actionEvent) throws IOException {
        returnToPrevious();
    }

    public void returnToPrevious () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("course-window.fxml"));
        ReturnHandler.returnMethod(fxmlLoader,(Stage)saveTitle.getScene().getWindow());
    }

}
