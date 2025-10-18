package org.yarmosh.model;

public class Region {
    private int id;
    private String name;
    private int square;
    private CitizenType citizenType;

    public Region(int id, String name, int square, CitizenType citizenType) {
        this.id = id;
        this.name = name;
        this.square = square;
        this.citizenType = citizenType;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getSquare() {
        return square;
    }
    public int getCitizenType() {
        return citizenType.getId();
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSquare(int square) {
        this.square = square;
    }
    public void setCitizenType(CitizenType citizenType) {
        this.citizenType = citizenType;
    }
}
