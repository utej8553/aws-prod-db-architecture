package com.dbarch.main.model;

public class Student {

    private String name;
    private String roll;
    private String branch;

    public Student() {
    }

    public Student(String name, String roll, String branch) {
        this.name = name;
        this.roll = roll;
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}