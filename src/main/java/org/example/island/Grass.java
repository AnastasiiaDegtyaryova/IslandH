package org.example.island;

import lombok.Getter;
import org.example.config.ConfigLoader;

@Getter
public class Grass {
    private final String name;
    private double weight;
    private final double growthRate;
    private static final double MAX_WEIGHT;

    static {
        MAX_WEIGHT = ConfigLoader.getIntProperty("grass.max.weight");
    }

    public Grass() {
        this.name = "Grass";
        this.weight = ConfigLoader.getIntProperty("grass.initial.weight");
        this.growthRate = ConfigLoader.getIntProperty("grass.growth.rate");
    }

    public void grow() {
        if (weight < MAX_WEIGHT) {
            weight += growthRate;
        }
    }
}
