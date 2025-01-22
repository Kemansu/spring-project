package com.example.demo.dto.animal;

import java.util.List;

public class AnimalDtoCreateRequest {
    private List<Long> animalTypes;

    private double weight;

    private double length;

    private double height;

    private String gender;

    private int chipperId;

    private long chippingLocationId;


    public AnimalDtoCreateRequest() {
    }

    public List<Long> getAnimalTypes() {
        return animalTypes;
    }

    public void setAnimalTypes(List<Long> animalTypes) {
        this.animalTypes = animalTypes;
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
