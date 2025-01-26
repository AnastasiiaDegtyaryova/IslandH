package org.example.animals.predators;

import java.util.HashMap;
import java.util.Map;
import org.example.animals.AnimalType;
import org.example.simulation.EatingChancesManager;

public class Fox extends Predator {

    public Fox(String name) {
        super(name);
        eatingChances = new HashMap<>();
        Map<AnimalType, Integer> chances = EatingChancesManager.getChancesForPredator(AnimalType.FOX);

        for (Map.Entry<AnimalType, Integer> entry : chances.entrySet()) {
            eatingChances.put(entry.getKey().getAnimalClass(), entry.getValue());
        }
    }
}
