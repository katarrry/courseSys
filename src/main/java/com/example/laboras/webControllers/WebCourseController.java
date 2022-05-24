package com.example.laboras.webControllers;

import com.example.laboras.control.Constants;
import com.example.laboras.control.DbUtils;
import com.example.laboras.ds.Course;
import com.example.laboras.serializers.CourseGSONSerializer;
import com.example.laboras.serializers.CoursesGSONSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Controller
public class WebCourseController {
    @RequestMapping(value = "course/createCourse", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createCourse(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String title = data.getProperty("title");
        String desc = data.getProperty("desc");
        String startDate = data.getProperty("startDate");
        String endDate = data.getProperty("endDate");
        String id = data.getProperty("id");

        DbUtils.addCourse(title, desc, LocalDate.parse(startDate), LocalDate.parse(endDate), Integer.parseInt(id));
        return "Course created";
    }

    @RequestMapping(value = "course/updateCourseTitle", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCourseTitle(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String title = data.getProperty("title");
        String id = data.getProperty("id");
        DbUtils.updateTableString("courses", "title", title, Integer.parseInt(id));
        return "Course updated";
    }

    @RequestMapping(value = "course/updateCourseDescription", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCourseDesc(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String desc = data.getProperty("desc");
        String id = data.getProperty("id");
        DbUtils.updateTableString("courses", "description", desc, Integer.parseInt(id));
        return "Course updated";
    }

    @RequestMapping(value = "course/updateCourseStartDate", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCourseStartDate(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String startDate = data.getProperty("startDate");
        String id = data.getProperty("id");
        DbUtils.updateTableDate("courses", "start_date", LocalDate.parse(startDate), Integer.parseInt(id));
        return "Course updated";
    }

    @RequestMapping(value = "course/updateCourseEndDate", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCourseEndDate(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String endDate = data.getProperty("endDate");
        String id = data.getProperty("id");
        DbUtils.updateTableDate("courses", "end_date", LocalDate.parse(endDate), Integer.parseInt(id));
        return "Course updated";
    }

    @RequestMapping(value = "course/getMyCourses", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCoursesById(@RequestParam("id") String id) {
        List<Course> courses = DbUtils.getCoursesByUser(Integer.parseInt(id));
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Course.class, new CourseGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(courses.get(0));

        Type libraryList = new TypeToken<List<Course>>() {
        }.getType();
        gson.registerTypeAdapter(libraryList, new CoursesGSONSerializer());
        parser = gson.create();
        return parser.toJson(courses);
    }

    @RequestMapping(value = "course/getAllCourses", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCourses() {
        List<Course> courses = DbUtils.getAllCourses();
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Course.class, new CourseGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(courses.get(0));

        Type libraryList = new TypeToken<List<Course>>() {
        }.getType();
        gson.registerTypeAdapter(libraryList, new CoursesGSONSerializer());
        parser = gson.create();
        return parser.toJson(courses);
    }

    @RequestMapping(value = "course/deleteCourse", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCourse(@RequestParam("id") String id) {
        DbUtils.deleteCourseFromDb(Integer.parseInt(id));
        return "Course deleted";
    }

}
