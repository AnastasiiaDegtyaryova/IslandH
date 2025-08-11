package org.example;

import org.example.simulation.SimulationManager;

public class Main {
    public static void main(String[] args) {
        try {
            SimulationManager simulationManager = new SimulationManager();
            simulationManager.startSimulation();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

