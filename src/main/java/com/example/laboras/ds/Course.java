package com.example.laboras.ds;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

public class Course {
    private int id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private ArrayList<User> editors;
    private ArrayList<User> students;
    private int creator;

    public Course(int id, String title) {
        this.id = id;
        this.title = title;
    }


    public Course(int id, String title, String description, Date startDate, Date endDate, ArrayList<User> editors) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.editors = editors;
    }

    public Course(int id, String title, String description, Date startDate, Date endDate, int creator) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.creator = creator;
    }

    public Course(int id, String title, String description, Date startDate, Date endDate, ArrayList<User> editors, ArrayList<User> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.editors = editors;
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ArrayList<User> getEditors() {
        return editors;
    }

    public void setEditors(ArrayList<User> editors) {
        this.editors = editors;
    }

    public ArrayList<User> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<User> students) {
        this.students = students;
    }
}
