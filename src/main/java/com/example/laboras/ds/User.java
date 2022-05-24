package com.example.laboras.ds;

public abstract class User {
    private int id;
    private String name;
    private String surname;
    private String login;
    private String psw;
    private String email;

    public User(int id, String name, String surname, String login, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
    }

    public User(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, String name, String surname, String login, String psw, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.psw = psw;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
