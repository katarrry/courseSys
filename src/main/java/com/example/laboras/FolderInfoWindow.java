package com.example.laboras;

import com.example.laboras.control.Constants;
import com.example.laboras.control.DbUtils;
import com.example.laboras.ds.Folder;
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

public class FolderInfoWindow implements Initializable {
    @FXML
    public Label id;
    @FXML
    public Label fileCount;
    @FXML
    public Label dateUpdated;
    @FXML
    public Label dateCreated;
    @FXML
    public Label title;
    @FXML
    public TextField newTitle;
    @FXML
    public Button saveTitle;
    @FXML
    public Button deleteFolder;
    @FXML
    public Button back;
    @FXML
    public Button addFile;
    @FXML
    public TextField fileTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Folder folder = DbUtils.getFolderInfo(Constants.folderId);
        id.setText("ID: "+folder.getId());
        title.setText("Title: "+folder.getTitle());
        dateCreated.setText("Date created: "+folder.getDateCreated());
        dateUpdated.setText("Date updated: "+folder.getDateUpdated());
        fileCount.setText("Files inside folder: "+DbUtils.getFolderFilesCount(Constants.folderId));

        if (!(DbUtils.isCourseCreator(Constants.userId, folder.getParentCourse()) || Constants.userType.equals("A"))) {
            newTitle.setVisible(false);
            saveTitle.setVisible(false);
            deleteFolder.setVisible(false);
            addFile.setVisible(false);
            fileTitle.setVisible(false);
        }
        if (DbUtils.isModerator(Constants.userId, folder.getParentCourse())) {
            addFile.setVisible(true);
            fileTitle.setVisible(true);
        }
    }

    public void updateFolderTitle(ActionEvent actionEvent) {
        if (!newTitle.getText().equals("")) {
            DbUtils.updateTableString("folders", "title", newTitle.getText(), Constants.folderId);
            title.setText("Title: "+newTitle.getText());
            DbUtils.updateTableDate("files", "date_updated", LocalDate.now(), Constants.folderId);
            dateUpdated.setText("Date updated: "+LocalDate.now());
            LoginWindow.alertMessage("Folder Updated!");
        }
        else {
            LoginWindow.alertMessage("Please provide folder title");
        }
    }

    public void deleteFolder(ActionEvent actionEvent) {
        DbUtils.deleteFolderFromDb(Constants.folderId);
        LoginWindow.alertMessage("Folder deleted");
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
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) title.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void addFile(ActionEvent actionEvent) {
        if (fileTitle.getText().equals(""))
            LoginWindow.alertMessage("Missing file title");
        else {
            DbUtils.addFile(fileTitle.getText(), Constants.folderId);
            LoginWindow.alertMessage("File added");
            fileCount.setText("Files inside folder: "+DbUtils.getFolderFilesCount(Constants.folderId));
        }
    }
}
