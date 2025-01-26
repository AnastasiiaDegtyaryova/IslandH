package org.example.animals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalData {
    private AnimalType type;
    private String name;
    private double weight;
    private int maxPopulation;
    private int maxSpeed;
    private double foodRequirement;
}
