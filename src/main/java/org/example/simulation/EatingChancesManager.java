package org.example.simulation;

import org.example.animals.AnimalType;
import org.example.config.YAMLLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EatingChancesManager {
    private static Map<AnimalType, Map<AnimalType, Integer>> eatingChances;

    public static void initialize(String yamlFilePath) {
        YAMLLoader loader = new YAMLLoader();
        Map<String, Map<String, Integer>> rawChances = loader.loadEatingChances(yamlFilePath);

        eatingChances = new HashMap<>();
        rawChances.forEach((predatorKey, preyMap) -> {
            AnimalType predator = AnimalType.valueOf(predatorKey.toUpperCase());
            Map<AnimalType, Integer> chances = preyMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> AnimalType.valueOf(entry.getKey().toUpperCase()),
                            Map.Entry::getValue
                    ));
            eatingChances.put(predator, chances);
        });
    }

    public static Map<AnimalType, Integer> getChancesForPredator(AnimalType predator) {
        if (eatingChances == null) {
            throw new IllegalStateException("Eating chances have not been initialized. Call EatingChancesManager.initialize() first.");
        }
        return eatingChances.getOrDefault(predator, Map.of());
    }
}

