package org.example.animals.predators;

import org.example.animals.AnimalType;
import org.example.simulation.EatingChancesManager;
import java.util.HashMap;
import java.util.Map;

public class Boa extends Predator {

    public Boa(String name) {
        super(name);
        eatingChances = new HashMap<>();
        Map<AnimalType, Integer> chances = EatingChancesManager.getChancesForPredator(AnimalType.BOA);

        for (Map.Entry<AnimalType, Integer> entry : chances.entrySet()) {
            eatingChances.put(entry.getKey().getAnimalClass(), entry.getValue());
        }
    }
}
