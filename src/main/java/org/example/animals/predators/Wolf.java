package org.example.animals.predators;

import org.example.animals.AnimalType;
import org.example.simulation.EatingChancesManager;
import java.util.HashMap;
import java.util.Map;

public class Wolf extends Predator {

    public Wolf(String name) {
        super(name);
        eatingChances = new HashMap<>();
        Map<AnimalType, Integer> chances = EatingChancesManager.getChancesForPredator(AnimalType.WOLF);

        for (Map.Entry<AnimalType, Integer> entry : chances.entrySet()) {
            eatingChances.put(entry.getKey().getAnimalClass(), entry.getValue());
        }
    }
}


