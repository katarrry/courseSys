package com.example.laboras.ds;

public class Company extends User {
    private String title;

    public Company(int id, String name, String surname, String login, String psw, String email, String title) {
        super(id, name, surname, login, psw, email);
        this.title = title;
    }

    public Company(int id, String name, String surname, String login, String email, String title) {
        super(id, name, surname, login, email);
        this.title = title;
    }

    public Company(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
