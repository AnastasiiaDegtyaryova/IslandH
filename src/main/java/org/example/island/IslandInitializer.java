package org.example.island;

import org.example.animals.Animal;
import org.example.animals.AnimalFactory;
import org.example.animals.AnimalData;
import org.example.config.SimulationConfig;
import org.example.config.YAMLLoader;
import org.example.simulation.EatingChancesManager;
import java.util.List;
import java.util.Random;

public class IslandInitializer {

    public static Island initialize(SimulationConfig config) {
        EatingChancesManager.initialize(config.getEatingChancesFile());
        List<AnimalData> animals = loadAnimalData(config.getAnimalDataFile());
        Island island = new Island(config.getIslandWidth(), config.getIslandHeight());

        populateAnimals(island, animals, config.getAnimalsPerType());
        populatePlants(island);

        return island;
    }

    private static List<AnimalData> loadAnimalData(String animalDataFile) {
        YAMLLoader yamlLoader = new YAMLLoader();
        return yamlLoader.loadAnimalData(animalDataFile);
    }

    private static void populateAnimals(Island island, List<AnimalData> animals, int animalsPerType) {
        Random rand = new Random();
        for (AnimalData animalData : animals) {
            for (int i = 0; i < animalsPerType; i++) {
                Animal animal = AnimalFactory.createAnimal(animalData);
                if (animal != null) {
                    int x = rand.nextInt(island.getWidth());
                    int y = rand.nextInt(island.getHeight());
                    island.addAnimalToCell(x, y, animal);
                }
            }
        }
    }

    private static void populatePlants(Island island) {
        for (int i = 0; i < island.getWidth(); i++) {
            for (int j = 0; j < island.getHeight(); j++) {
                Grass grass = new Grass();
                island.addPlantToCell(i, j, grass);
            }
        }
    }
}
