package com.example.laboras;

import com.example.laboras.control.*;
import com.example.laboras.ds.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.laboras.control.DbUtils.*;

public class CourseWindow implements Initializable {
    @FXML
    public Button addNewCourseB;
    @FXML
    public ListView myCourses;
    @FXML
    public MenuItem viewCourse;
    @FXML
    public ListView myFolders;
    @FXML
    public MenuItem viewFolder;
    @FXML
    public ListView myFiles;
    @FXML
    public MenuItem viewFile;
    @FXML
    public ListView allCourses;
    @FXML
    public MenuItem enroll;
    @FXML
    public Tab myCoursesTab;
    @FXML
    public Tab allCoursesTab;
    @FXML
    public Tab allUsersTab;
    @FXML
    public Button userInfoB;
    @FXML
    public ListView allCompanies;
    @FXML
    public ListView allPerson;
    @FXML
    public MenuItem editCompany;
    @FXML
    public MenuItem editPerson;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       Constants.userType =  UserDbUtils.getUserType(Constants.userId);
       viewFolder.setVisible(false);
       viewCourse.setVisible(false);
       viewFile.setVisible(false);
       enroll.setVisible(false);
       editCompany.setVisible(false);
       editPerson.setVisible(false);

       if (Constants.userType.equals("P")) {
           allUsersTab.setDisable(true);

           allCourses.getItems().clear();
           ArrayList<Course> allCoursesDb = CourseDbUtils.getAllCourses();

           for (Course c : allCoursesDb) {
               allCourses.getItems().add(c.getId() + ":" + c.getTitle());
           }
           if (!allCourses.getItems().isEmpty()) {
               enroll.setVisible(true);
           }

       }
       else if (Constants.userType.equals("C")) {
           allUsersTab.setDisable(true);
           allCoursesTab.setDisable(true);
       }
       else {
           allCoursesTab.setText("---");
           allCoursesTab.setDisable(true);
         //  addNewCourseB.setVisible(false);
           myCoursesTab.setText("All courses");

           allCompanies.getItems().clear();
           ArrayList<Company> allCompaniesDb = CompanyDbUtils.getAllCompanies();

           for (Company c : allCompaniesDb) {
               allCompanies.getItems().add(c.getId() + ":" + c.getTitle());
           }

           if (!allCompanies.getItems().isEmpty()) {
               editCompany.setVisible(true);
           }

           allPerson.getItems().clear();
           ArrayList<Person> allPersonDb = PersonDbUtils.getAllPerson();

           for (Person p : allPersonDb) {
               allPerson.getItems().add(p.getId() + ":" + p.getName() + ":" + p.getSurname());
           }

           if (!allPerson.getItems().isEmpty()) {
               editPerson.setVisible(true);
           }

       }

        myCourses.getItems().clear();
        ArrayList<Course> myCoursesDb;
        if (Constants.userType.equals("A")) {
            myCoursesDb = CourseDbUtils.getAllCourses();
        }
        else {
            myCoursesDb = CourseDbUtils.getCoursesByUser(Constants.userId);
        }
        for (Course c : myCoursesDb) {
            myCourses.getItems().add(c.getId() + ":" + c.getTitle());
        }
        if (!myCourses.getItems().isEmpty()) {
            viewCourse.setVisible(true);
        }


    }

    public void populateFolders(MouseEvent mouseEvent) {
        try {
            String courseIds = myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
            Constants.courseId = Integer.parseInt(courseIds);
            myFolders.getItems().clear();
            ArrayList<Folder> myFoldersDb = FolderDbUtils.getFoldersByCourseId(Constants.courseId);
            for (Folder f : myFoldersDb) {
                myFolders.getItems().add(f.getId() + ":" + f.getTitle());
            }
            if (!myFolders.getItems().isEmpty()) {
                viewFolder.setVisible(true);
            }
            myFiles.getItems().clear();

        } catch (RuntimeException e) {

        }
    }

    public void populateFiles(MouseEvent mouseEvent) {
        try {
            String folderIds = myFolders.getSelectionModel().getSelectedItem().toString().split(":")[0];
            Constants.folderId = Integer.parseInt(folderIds);
            myFiles.getItems().clear();
            ArrayList<File> myFilesDb = FileDbUtils.getFilesByFolderId(Constants.folderId);
            for (File f : myFilesDb) {
                myFiles.getItems().add(f.getId() + ":" + f.getTitle());
            }
            if (!myFiles.getItems().isEmpty()) {
                viewFile.setVisible(true);
            }

        } catch (RuntimeException e) {

        }

    }

    public void openCreateCourse(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("create_course_window.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) myCourses.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void openCourseInfo(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("course-info-window.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) myCourses.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void openFolderInfo(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("folder-info-window.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) myFolders.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void openFileInfo(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("file-info-window.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) myFiles.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void enrollUser(ActionEvent actionEvent) {
        UserDbUtils.enroll(Constants.userId, Constants.courseId);
        myCourses.getItems().clear();
        ArrayList<Course> myCoursesDb;
        myCoursesDb = CourseDbUtils.getCoursesByUser(Constants.userId);
        for (Course c : myCoursesDb) {
            myCourses.getItems().add(c.getId() + ":" + c.getTitle());
        }
        viewCourse.setVisible(true);
        LoginWindow.alertMessage("You have been enrolled!");
    }

    public void openEditUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("user-info-window.fxml"));
        ReturnHandler.returnMethod(fxmlLoader,(Stage)allCompanies.getScene().getWindow());
    }

    public void openEditMyUser(ActionEvent actionEvent) throws IOException {
        Constants.listUserId = Constants.userId;
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("user-info-window.fxml"));
        ReturnHandler.returnMethod(fxmlLoader,(Stage)allCourses.getScene().getWindow());
    }

    public void setFile(MouseEvent mouseEvent) {
        String fileIds = myFiles.getSelectionModel().getSelectedItem().toString().split(":")[0];
        Constants.fileId = Integer.parseInt(fileIds);
    }

    public void setCourse(MouseEvent mouseEvent) {
        String courseIds = allCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
        Constants.courseId = Integer.parseInt(courseIds);
    }

    public void setCompany(MouseEvent mouseEvent) {
        String userIds = allCompanies.getSelectionModel().getSelectedItem().toString().split(":")[0];
        Constants.listUserId = Integer.parseInt(userIds);
    }

    public void setPerson(MouseEvent mouseEvent) {
        String userIds = allPerson.getSelectionModel().getSelectedItem().toString().split(":")[0];
        Constants.listUserId = Integer.parseInt(userIds);
    }
}
