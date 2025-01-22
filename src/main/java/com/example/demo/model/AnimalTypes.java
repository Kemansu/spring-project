package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "animal_types")
public class AnimalTypes {

    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne()
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne()
    @JoinColumn(name = "animal_type_id")
    private AnimalType animalType;

    public AnimalTypes() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }
}
