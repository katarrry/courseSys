package com.example.laboras.ds;

import java.sql.Date;
import java.time.LocalDate;

public class File {
    private int id;
    private String title;
    private int parentFolder;
    private Date dateCreated;
    private Date dateUpdated;

    public File(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public File(int id, String title, int parentFolder, Date dateCreated, Date dateUpdated) {
        this.id = id;
        this.title = title;
        this.parentFolder = parentFolder;
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

    public int getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(int parentFolder) {
        this.parentFolder = parentFolder;
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
