package com.example.laboras.ds;

public class Person extends User{
    private boolean type;

    public Person(int id, String name, String surname, String login, String psw, String email, boolean type) {
        super(id, name, surname, login, psw, email);
        this.type = type;
    }

    public Person(int id, String name, String surname, String login, String email, boolean type) {
        super(id, name, surname, login, email);
        this.type = type;
    }

    public Person(int id, String name, String surname, String login, String psw, String email) {
        super(id, name, surname, login, psw, email);
    }

    public Person(int id, String name, String surname) {
        super(id, name, surname);
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
