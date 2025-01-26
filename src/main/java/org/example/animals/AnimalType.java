package org.example.animals;

import org.example.animals.herbivores.*;
import org.example.animals.predators.*;

public enum AnimalType {
    WOLF(Wolf.class),
    BEAR(Bear.class),
    BOA(Boa.class),
    EAGLE(Eagle.class),
    FOX(Fox.class),
    HORSE(Horse.class),
    RABBIT(Rabbit.class),
    BUFFALO(Buffalo.class),
    CATERPILLAR(Caterpillar.class),
    DEER(Deer.class),
    DUCK(Duck.class),
    GOAT(Goat.class),
    HOG(Hog.class),
    MOUSE(Mouse.class),
    SHEEP(Sheep.class);

    private final Class<? extends Animal> animalClass;

    AnimalType(Class<? extends Animal> animalClass) {
        this.animalClass = animalClass;
    }

    public Class<? extends Animal> getAnimalClass() {
        return animalClass;
    }
}

