package com.example.laboras;

import com.example.laboras.control.Constants;
import com.example.laboras.control.DbUtils;
import com.example.laboras.ds.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
    private static final String USER_TYPE_PERSON = "P";
    private static final String USER_TYPE_ADMIN = "A";
    private static final String CREATE_COURSE_WINDOW_FXML = "create_course_window.fxml";
    private static final String COURSE_INFO_WINDOW_FXML = "course-info-window.fxml";
    private static final String FOLDER_INFO_WINDOW_FXML = "folder-info-window.fxml";
    private static final String FILE_INFO_WINDOW_FXML = "file-info-window.fxml";
    private static final String USER_INFO_WINDOW_FXML = "user-info-window.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constants.userType = getUserType(Constants.userId);
        viewFolder.setVisible(false);
        viewCourse.setVisible(false);
        viewFile.setVisible(false);
        enroll.setVisible(false);
        editCompany.setVisible(false);
        editPerson.setVisible(false);

        allUsersTab.setDisable(true);
        allCoursesTab.setDisable(true);
        myCoursesTab.setText("---");
        myCoursesTab.setDisable(true);

        switch (Constants.userType) {
            case USER_TYPE_PERSON -> populateAllCoursesList();
            case USER_TYPE_ADMIN -> {
                populateCompaniesList();
                populatePersonList();
                myCoursesTab.setText("All courses");
                myCoursesTab.setDisable(false);
                populateMyCoursesList();
            }
        }
    }

    private void populateAllCoursesList() {
        List<Course> allCoursesDb = DbUtils.getAllCourses();
        if (!allCoursesDb.isEmpty()) {
            allCourses.getItems().clear();
            for (Course c : allCoursesDb) {
                allCourses.getItems().add(c.getId() + ":" + c.getTitle());
            }
            enroll.setVisible(true);
        }
    }

    private void populateCompaniesList() {
        List<Company> allCompaniesDb = DbUtils.getAllCompanies();
        if (!allCompaniesDb.isEmpty()) {
            allCompanies.getItems().clear();
            for (Company c : allCompaniesDb) {
                allCompanies.getItems().add(c.getId() + ":" + c.getTitle());
            }
            editCompany.setVisible(true);
        }
    }

    private void populatePersonList() {
        List<Person> allPersonDb = DbUtils.getAllPerson();
        if (!allPersonDb.isEmpty()) {
            allPerson.getItems().clear();
            for (Person p : allPersonDb) {
                allPerson.getItems().add(p.getId() + ":" + p.getName() + ":" + p.getSurname());
            }
            editPerson.setVisible(true);
        }
    }

    private void populateMyCoursesList() {
        List<Course> myCoursesDb;
        if (Constants.userType.equals(USER_TYPE_ADMIN)) {
            myCoursesDb = DbUtils.getAllCourses();
        } else {
            myCoursesDb = DbUtils.getCoursesByUser(Constants.userId);
        }
        if (!myCoursesDb.isEmpty()) {
            myCourses.getItems().clear();
            for (Course c : myCoursesDb) {
                myCourses.getItems().add(c.getId() + ":" + c.getTitle());
            }
            viewCourse.setVisible(true);
        }
    }

    private List<Folder> retrieveFolders() {
        String courseIds = myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
        Constants.courseId = Integer.parseInt(courseIds);
        return DbUtils.getFoldersByCourseId(Constants.courseId);
    }

    private void updateFoldersUI(List<Folder> folders) {
        myFolders.getItems().clear();
        for (Folder f : folders) {
            myFolders.getItems().add(f.getId() + ":" + f.getTitle());
        }
        if (!myFolders.getItems().isEmpty()) {
            viewFolder.setVisible(true);
        }
        myFiles.getItems().clear();
    }

    private void handleFoldersError(RuntimeException e) {
        LoginWindow.alertMessage("Cannot populate folders!");
    }

    @FXML
    private void populateFoldersList(MouseEvent mouseEvent) {
        try {
            List<Folder> folders = retrieveFolders();
            updateFoldersUI(folders);
        } catch (RuntimeException e) {
            handleFoldersError(e);
        }
    }

    @FXML
    private void populateFilesList(MouseEvent mouseEvent) {
        try {
            String folderIds = myFolders.getSelectionModel().getSelectedItem().toString().split(":")[0];
            Constants.folderId = Integer.parseInt(folderIds);
            myFiles.getItems().clear();
            List<File> myFilesDb = DbUtils.getFilesByFolderId(Constants.folderId);
            for (File f : myFilesDb) {
                myFiles.getItems().add(f.getId() + ":" + f.getTitle());
            }
            if (!myFiles.getItems().isEmpty()) {
                viewFile.setVisible(true);
            }
        } catch (RuntimeException e) {
            LoginWindow.alertMessage("Cannot populate files!");
        }
    }

    private void openWindow(String fxmlFile, Node source) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource(fxmlFile));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void openCreateCourse(ActionEvent actionEvent) throws IOException {
        openWindow(CREATE_COURSE_WINDOW_FXML, myCourses);
    }

    public void openCourseInfo(ActionEvent actionEvent) throws IOException {
        openWindow(COURSE_INFO_WINDOW_FXML, myCourses);
    }

    public void openFolderInfo(ActionEvent actionEvent) throws IOException {
        openWindow(FOLDER_INFO_WINDOW_FXML, myFolders);
    }

    public void openFileInfo(ActionEvent actionEvent) throws IOException {
        openWindow(FILE_INFO_WINDOW_FXML, myFiles);
    }

    public void enrollUser(ActionEvent actionEvent) {
        enroll(Constants.userId, Constants.courseId);
        myCourses.getItems().clear();
        List<Course> myCoursesDb = DbUtils.getCoursesByUser(Constants.userId);
        for (Course c : myCoursesDb) {
            myCourses.getItems().add(c.getId() + ":" + c.getTitle());
        }
        viewCourse.setVisible(true);
        LoginWindow.alertMessage("You have been enrolled!");
    }

    public void openEditUser(ActionEvent actionEvent) throws IOException {
        openWindow(USER_INFO_WINDOW_FXML, allCompanies);
    }

    public void openEditMyUser(ActionEvent actionEvent) throws IOException {
        Constants.listUserId = Constants.userId;
        openWindow(USER_INFO_WINDOW_FXML, myCourses);
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
