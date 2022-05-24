package com.example.laboras.ds;

import java.sql.Date;
import java.util.ArrayList;

public class Folder {
    private int id;
    private String title;
    private ArrayList<File> files;
    private int parentCourse;
    private Date dateCreated;
    private Date dateUpdated;

    public Folder(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Folder(int id, String title, ArrayList<File> files, int parentCourseId, Date dateCreated, Date dateUpdated) {
        this.id = id;
        this.title = title;
        this.files = files;
        this.parentCourse = parentCourseId;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public Folder(int id, String title, int parentCourseId, Date dateCreated, Date dateUpdated) {
        this.id = id;
        this.title = title;
        this.parentCourse = parentCourseId;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
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

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public int getParentCourse() {
        return parentCourse;
    }

    public void setParentCourse(int parentCourseId) {
        this.parentCourse = parentCourseId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
