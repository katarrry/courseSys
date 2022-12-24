package com.example.laboras;

import com.example.laboras.control.*;
import com.example.laboras.ds.Company;
import com.example.laboras.ds.Course;
import com.example.laboras.ds.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CourseInfoWindow implements Initializable {
    @FXML
    public Label courseId;
    @FXML
    public Text courseTitle;
    @FXML
    public Text courseDesc;
    @FXML
    public Text courseCreator;
    @FXML
    public Text startDate;
    @FXML
    public Text endDate;
    @FXML
    public ListView editorList;
    @FXML
    public MenuItem removeEditor;
    @FXML
    public ListView studentList;
    @FXML
    public MenuItem removeStudent;
    @FXML
    public Button updateCourse;
    @FXML
    public Button deleteCourse;
    @FXML
    public Button dropOut;
    @FXML
    public TextField editorId;
    @FXML
    public Button addEditor;
    @FXML
    public TextField studentId;
    @FXML
    public Button addStudent;
    @FXML
    public Button back;
    @FXML
    public Button addFolder;
    @FXML
    public TextField folderTitle;

    int selectedEditorId=0;
    int selectedStudentId=0;
    boolean isCreator=false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isCreator = CourseDbUtils.isCourseCreator(Constants.userId, Constants.courseId);
        removeEditor.setVisible(false);
        removeStudent.setVisible(false);
        addFolder.setVisible(false);
        folderTitle.setVisible(false);

        if (Constants.userType.equals("A") || isCreator) {
            dropOut.setVisible(false);
            addFolder.setVisible(true);
            folderTitle.setVisible(true);
        }
        else {
            updateCourse.setVisible(false);
            deleteCourse.setVisible(false);
            editorId.setVisible(false);
            addEditor.setVisible(false);
            studentId.setVisible(false);
            addStudent.setVisible(false);
        }
        if (UserDbUtils.isModerator(Constants.userId, Constants.courseId)) {
            dropOut.setText("Remove editor rights");
            addFolder.setVisible(true);
            folderTitle.setVisible(true);
        }
        Course course = CourseDbUtils.getCourseInfo(Constants.courseId);

        if(course != null) {
            courseId.setText("Course id: " + course.getId());
            courseTitle.setText("Course title: "+course.getTitle());
            courseDesc.setText("Description: "+course.getDescription());
        }
        int creatorId = CourseDbUtils.getCourseCreator(Constants.courseId);
        if (UserDbUtils.getUserType(creatorId).equals("C")) {
            Company company = CompanyDbUtils.getCompanyInfo(creatorId);
            if(company != null) {
                courseCreator.setText("Course creator: " + company.getTitle());
            }
        }
        else {
            Person person = PersonDbUtils.getPersonInfo(creatorId);
            if(person != null) {
                courseCreator.setText("Course creator: " + person.getName() + " " + person.getSurname());
            }
        }
        if(course != null) {
            startDate.setText("Start date: " + course.getStartDate());
            endDate.setText("End date: " + course.getEndDate());
        }
        fillEditorTable();
        fillStudentTable();
    }

    public void removeEditor(ActionEvent actionEvent) {
        UserDbUtils.updateEditor(selectedEditorId, Constants.courseId);
        if (UserDbUtils.getUserType(selectedEditorId).equals("C"))
            UserDbUtils.deleteUserFromCourseDb(selectedEditorId, Constants.courseId);
        LoginWindow.alertMessage("Editor rights removed");
        fillEditorTable();
        fillStudentTable();
    }

    public void removeStudent(ActionEvent actionEvent) {
        UserDbUtils.deleteUserFromCourseDb(selectedStudentId, Constants.courseId);
        LoginWindow.alertMessage("Student removed");
        fillStudentTable();
    }

    public void openUpdate(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("update_course_window.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) courseTitle.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void deleteCourse(ActionEvent actionEvent) {
        CourseDbUtils.deleteCourseFromDb(Constants.courseId);
        LoginWindow.alertMessage("Course deleted");
        try {
            returnToPrevious();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dropOut(ActionEvent actionEvent) {
        if (UserDbUtils.isModerator(Constants.userId, Constants.courseId) && Constants.userType.equals("P")) {
            UserDbUtils.updateEditor(Constants.userId, Constants.courseId);
        }
        else {
            UserDbUtils.deleteUserFromCourseDb(Constants.userId, Constants.courseId);
        }

        LoginWindow.alertMessage("Dropped out from course");
        try {
            returnToPrevious();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillEditorTable() {
        editorList.getItems().clear();
        ArrayList<Person> personEditors = PersonDbUtils.getCourseEditorsP(Constants.courseId);
        for (Person p : personEditors) {
            editorList.getItems().add(p.getId() + ":" + p.getName()+":"+p.getSurname());
        }
        ArrayList<Company> companyEditors = CompanyDbUtils.getCourseEditorsC(Constants.courseId);
        for (Company c : companyEditors) {
            editorList.getItems().add(c.getId() + ":" + c.getTitle());
        }
        if (!editorList.getItems().isEmpty() && (Constants.userType.equals("A") || isCreator)) {
            removeEditor.setVisible(true);
        }
    }

    public void fillStudentTable() {
        studentList.getItems().clear();
        ArrayList<Person> students = PersonDbUtils.getCourseStudents(Constants.courseId);
        for (Person p : students) {
                studentList.getItems().add(p.getId() + ":" + p.getName() + ":" + p.getSurname());
        }
        if (!studentList.getItems().isEmpty() && (Constants.userType.equals("A") || isCreator)) {
            removeStudent.setVisible(true);
        }
    }

    public void addEditor(ActionEvent actionEvent) {
        if (isInteger(editorId.getText())) {
            if (!CourseDbUtils.isCourseCreator(Integer.parseInt(editorId.getText()), Constants.courseId) && !UserDbUtils.getUserType(Integer.parseInt(editorId.getText())).equals("A")) {
                if (UserDbUtils.isStudentEnrolled(Integer.parseInt(editorId.getText()), Constants.courseId)) {
                    UserDbUtils.grantEditorRights(Integer.parseInt(editorId.getText()), Constants.courseId);
                    LoginWindow.alertMessage("Editor rights have been granted");
                }
                else if (UserDbUtils.getUserType(Integer.parseInt(editorId.getText())).equals("")) {
                    LoginWindow.alertMessage("Such user does not exist");
                }
                else {
                    UserDbUtils.addEditor(Constants.courseId, Integer.parseInt(editorId.getText()));
                    LoginWindow.alertMessage("Editor added");
                }
                fillStudentTable();
                fillEditorTable();
            }
            else {
                LoginWindow.alertMessage("This user cannot be added");
            }
        }
        else {
            LoginWindow.alertMessage("Id is not a number");
        }
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void addStudent(ActionEvent actionEvent) {
        if (isInteger(studentId.getText())) {
            if (UserDbUtils.getUserType(Integer.parseInt(studentId.getText())).equals("P") && !UserDbUtils.isModerator(Integer.parseInt(studentId.getText()), Constants.courseId) && !CourseDbUtils.isCourseCreator(Integer.parseInt(studentId.getText()), Constants.courseId)) {
                UserDbUtils.enroll(Integer.parseInt(studentId.getText()), Constants.courseId);
                LoginWindow.alertMessage("Student added");
                fillStudentTable();
            }
            else LoginWindow.alertMessage("This user is a company or admin or already an editor or does not exist");
        }
        else {
            LoginWindow.alertMessage("Id is not a number");
        }
    }

    public void returnToCourse(ActionEvent actionEvent) throws IOException {
        returnToPrevious();
    }

    public void returnToPrevious () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("course-window.fxml"));
        ReturnHandler.returnMethod(fxmlLoader,(Stage)courseTitle.getScene().getWindow());
    }

    public void setEditor(MouseEvent mouseEvent) {
        String editorIds = editorList.getSelectionModel().getSelectedItem().toString().split(":")[0];
        selectedEditorId = Integer.parseInt(editorIds);
    }

    public void setStudent(MouseEvent mouseEvent) {
        String studentIds = studentList.getSelectionModel().getSelectedItem().toString().split(":")[0];
        selectedStudentId = Integer.parseInt(studentIds);
    }

    public void addFolder(ActionEvent actionEvent) {
        if (folderTitle.getText().equals(""))
            LoginWindow.alertMessage("Missing folder title");
        else {
            FolderDbUtils.addFolder(folderTitle.getText(), Constants.courseId);
            LoginWindow.alertMessage("Folder added");
        }
    }
}
