package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "animal_types")
public class AnimalTypes {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne()
    @JoinColumn(name = "animal_type_id")
    private AnimalType animalType;

}
