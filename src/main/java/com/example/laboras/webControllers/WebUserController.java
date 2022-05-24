package com.example.laboras.webControllers;

import com.example.laboras.control.Constants;
import com.example.laboras.control.DbUtils;
import com.example.laboras.ds.Company;
import com.example.laboras.ds.Person;
import com.example.laboras.ds.User;
import com.example.laboras.serializers.UserGSONSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;



@Controller
public class WebUserController {

    @RequestMapping(value = "user/validateUser", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String validateLogin(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String loginName = data.getProperty("login");
        String password = data.getProperty("psw");
        int id = DbUtils.validateByCredentials(loginName, password);
        if (id==0) {
            return "Wrong credentials";
        }
        else {
            if (DbUtils.getUserType(id).equals("C")) {
                return id + "," + "C";
            }
            else if (DbUtils.getUserType(id).equals("A")) {
                return id + "," + "A";
            }
            else return id + "," + "P";
        }
    }

    @RequestMapping(value = "user/getUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public String getUserInfo(@RequestParam("id") String id) {
        GsonBuilder gson = new GsonBuilder();
        if (DbUtils.getUserType(Integer.parseInt(id)).equals("C")) {
            Company company = DbUtils.getCompanyInfo(Integer.parseInt(id));
            gson.registerTypeAdapter(Company.class, new UserGSONSerializer());
            Gson parser = gson.create();
            if (company == null) return "error getting user info";
            else return parser.toJson(company);
        }
        else {
            Person person = DbUtils.getPersonInfo(Integer.parseInt(id));
            gson.registerTypeAdapter(Person.class, new UserGSONSerializer());
            Gson parser = gson.create();
            if (person == null) return "error getting user info";
            else return parser.toJson(person);
        }
    }

    @RequestMapping(value = "user/getCompanyTitle", method = RequestMethod.GET)
    @ResponseBody
    public String getTitle(@RequestParam("id") String id) {
        if (DbUtils.getUserType(Integer.parseInt(id)).equals("C")) {
            Company company = DbUtils.getCompanyInfo(Integer.parseInt(id));
            if (company == null) return "error getting user info";
            else return company.getTitle();
        }
        else {
            return "User does not have a title";
        }
    }

    @RequestMapping(value = "user/createPerson", method = RequestMethod.POST)
    @ResponseBody
    public String createStudent(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String surname = data.getProperty("surname");
        String login = data.getProperty("login");
        String psw = data.getProperty("password");
        String email = data.getProperty("email");

        DbUtils.addPerson(name, surname, login, psw, email);
        return "Person created";
    }

    @RequestMapping(value = "user/createCompany", method = RequestMethod.POST)
    @ResponseBody
    public String createTeacher(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String surname = data.getProperty("surname");
        String login = data.getProperty("login");
        String psw = data.getProperty("password");
        String email = data.getProperty("email");
        String title = data.getProperty("title");
        DbUtils.addCompany(login, psw, name, surname, email, title);
        return "Company created";
    }

    @RequestMapping(value = "user/deleteUser", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteUser(@RequestParam("id") String id) {
        DbUtils.deleteUserFromDb(Integer.parseInt(id));
        return "User deleted";
    }

    @RequestMapping(value = "user/updateUserLogin", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUserLogin(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String id = data.getProperty("id");
        String login = data.getProperty("login");
        DbUtils.updateTableString("users", "login", login, Integer.parseInt(id));
        return "User updated";
    }

    @RequestMapping(value = "user/updateUser", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUser(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String id = data.getProperty("id");
        String name = data.getProperty("name");
        String surname = data.getProperty("surname");
        String login = data.getProperty("login");
        String psw = data.getProperty("psw");
        String email = data.getProperty("email");
        DbUtils.updateTableString("users", "login", login, Integer.parseInt(id));
        DbUtils.updateTableString("users", "psw", psw, Integer.parseInt(id));
        DbUtils.updateTableString("users", "name", name, Integer.parseInt(id));
        DbUtils.updateTableString("users", "surname", surname, Integer.parseInt(id));
        DbUtils.updateTableString("users", "email", email, Integer.parseInt(id));
        return "User updated";
    }

    @RequestMapping(value = "user/updateUserPassword", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUserPsw(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String id = data.getProperty("id");
        String psw = data.getProperty("psw");
        DbUtils.updateTableString("users", "psw", psw, Integer.parseInt(id));
        return "User updated";
    }

    @RequestMapping(value = "user/updateUserName", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUserName(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String id = data.getProperty("id");
        String name = data.getProperty("name");
        DbUtils.updateTableString("users", "name", name, Integer.parseInt(id));
        return "User updated";
    }

    @RequestMapping(value = "user/updateUserSurname", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUserSurname(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String id = data.getProperty("id");
        String surname = data.getProperty("surname");
        DbUtils.updateTableString("users", "surname", surname, Integer.parseInt(id));
        return "User updated";
    }

    @RequestMapping(value = "user/updateUserEmail", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUserEmail(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String id = data.getProperty("id");
        String email = data.getProperty("email");
        DbUtils.updateTableString("users", "email", email, Integer.parseInt(id));
        return "User updated";
    }

    @RequestMapping(value = "user/updateUserTitle", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUserTitle(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String id = data.getProperty("id");
        String title = data.getProperty("title");
        DbUtils.updateTableString("users", "title", title, Integer.parseInt(id));
        return "User updated";
    }

    @RequestMapping(value = "user/isInCourse", method = RequestMethod.GET)
    @ResponseBody
    public String isInCourse(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String courseId = data.getProperty("courseId");
        String userId = data.getProperty("userId");
        if(DbUtils.isStudentEnrolled(Integer.parseInt(userId), Integer.parseInt(courseId)))
            return "yes";
        else return "no";
    }

    @RequestMapping(value = "user/isAbleToDropOut", method = RequestMethod.GET)
    @ResponseBody
    public String canDropOut(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String courseId = data.getProperty("courseId");
        String userId = data.getProperty("userId");
        if (DbUtils.isCourseCreator(Integer.parseInt(userId), Integer.parseInt(courseId)) || DbUtils.isModerator(Integer.parseInt(userId), Integer.parseInt(courseId)) || !DbUtils.getUserType(Integer.parseInt(userId)).equals("P"))
        return "no";
        else return "yes";
    }

    @RequestMapping(value = "user/addUserToCourse", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addUserToCourse(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String courseId = data.getProperty("courseId");
        String id = data.getProperty("id");
        if (DbUtils.enroll(Integer.parseInt(id), Integer.parseInt(courseId)))
        return "User added to course";
        else return "Error";
    }

    @RequestMapping(value = "user/deleteUserFromCourse", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteUserFromCourse(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String id = data.getProperty("id");
        String courseId = data.getProperty("courseId");
        if (!(DbUtils.isCourseCreator(Integer.parseInt(id), Integer.parseInt(courseId)) || DbUtils.isModerator(Integer.parseInt(id), Integer.parseInt(courseId)) || !DbUtils.getUserType(Integer.parseInt(id)).equals("P")) && DbUtils.deleteUserFromCourseDb(Integer.parseInt(id), Integer.parseInt(courseId)))
        return "User deleted from course";
        else return "Error";
    }

}
