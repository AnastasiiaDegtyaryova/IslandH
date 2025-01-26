package org.example.config;

import lombok.Getter;

@Getter
public class SimulationConfig {
    private final int islandWidth = ConfigLoader.getIntProperty("island.width");
    private final int islandHeight = ConfigLoader.getIntProperty("island.height");
    private final String animalDataFile = ConfigLoader.getProperty("animal.data.file");
    private final String eatingChancesFile = ConfigLoader.getProperty("eating.chances.file");
    private final int animalsPerType = ConfigLoader.getIntProperty("animals.per.type");
    private final int plantGrowthInterval = ConfigLoader.getIntProperty("simulation.plant.growth.interval");
    private final int animalLifeCycleInterval = ConfigLoader.getIntProperty("simulation.animal.lifecycle.interval");
}

