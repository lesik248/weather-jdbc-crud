package org.yarmosh.model;

public class CitizenType {
    private int id;
    private String name;
    private String language;
    private int number;

    public CitizenType(int id, String name, String language, int number) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.number = number;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getLanguage() {
        return language;
    }
    public int getNumber() {
        return number;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public void setNumber(int number) {
        this.number = number;
    }
}
