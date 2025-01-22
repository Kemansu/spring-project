package com.example.demo.dto.animal;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public class AnimalDtoResponse {
    private long id;

    private List<Long> animalTypes;

    private double weight;

    private double length;

    private double height;

    private String gender;

    private String lifeStatus;

    private String chippingDateTime;

    private int chipperId;

    private long chippingLocationId;

    private List<Long> visitedLocations;

    private String deathDateTime;

    public AnimalDtoResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(String lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public String getChippingDateTime() {
        return chippingDateTime;
    }

    public void setChippingDateTime(String chippingDateTime) {
        this.chippingDateTime = chippingDateTime;
    }

    public String getDeathDateTime() {
        return deathDateTime;
    }

    public void setDeathDateTime(String deathDateTime) {
        this.deathDateTime = deathDateTime;
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

    public List<Long> getVisitedLocations() {
        return visitedLocations;
    }

    public void setVisitedLocations(List<Long> visitedLocation) {
        this.visitedLocations = visitedLocation;
    }


}
