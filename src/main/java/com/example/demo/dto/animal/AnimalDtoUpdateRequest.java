package com.example.demo.dto.animal;

public class AnimalDtoUpdateRequest {
    private double weight;

    private double length;

    private double height;

    private String gender;

    private String lifeStatus;

    private int chipperId;

    private long chippingLocationId;

    public AnimalDtoUpdateRequest() {
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(String lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public int getChipperId() {
        return chipperId;
    }

    public void setChipperId(int chipperId) {
        this.chipperId = chipperId;
    }

    public long getChippingLocationId() {
        return chippingLocationId;
    }

    public void setChippingLocationId(long chippingLocationId) {
        this.chippingLocationId = chippingLocationId;
    }
}
