package org.example.animals;

import java.lang.reflect.Constructor;

public class AnimalFactory {

    public static Animal createAnimal(AnimalData data) {
        try {
            AnimalType animalType = data.getType();
            Constructor<? extends Animal> constructor = animalType.getAnimalClass().getConstructor(String.class);
            return constructor.newInstance(data.getName());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown animal type: " + data.getType(), e);
        } catch (Exception e) {
            System.err.println("Failed to create animal: " + data.getName());
            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create animal: " + data.getName(), e);
        }
    }
}

