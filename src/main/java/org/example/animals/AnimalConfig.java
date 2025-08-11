package org.example.animals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalConfig {
    private static final Map<Class<? extends Animal>, AnimalAttributes> animalAttributesMap = new HashMap<>();

    public static void initialize(List<AnimalData> animalDataList) {
        for (AnimalData data : animalDataList) {
            AnimalType animalType = data.getType();
            AnimalAttributes attributes = new AnimalAttributes(
                    data.getWeight(),
                    data.getMaxPopulation(),
                    data.getMaxSpeed(),
                    data.getFoodRequirement()
            );
            animalAttributesMap.put(animalType.getAnimalClass(), attributes);
        }
    }

    public static AnimalAttributes getAttributes(Class<? extends Animal> animalClass) {
        return animalAttributesMap.get(animalClass);
    }

    public static int getMaxPopulation(Class<? extends Animal> animalClass) {
        return animalAttributesMap.get(animalClass).maxPopulation();
    }

    public static int getMaxSpeed(Class<? extends Animal> animalClass) {
        return animalAttributesMap.get(animalClass).maxSpeed();
    }

    public static double getWeight(Class<? extends Animal> animalClass) {
        return animalAttributesMap.get(animalClass).weight();
    }

    public static double getFoodRequirement(Class<? extends Animal> animalClass) {
        return animalAttributesMap.get(animalClass).foodRequirement();
    }
}

