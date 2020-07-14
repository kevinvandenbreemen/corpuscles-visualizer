package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.Corpuscle;
import com.vandenbreemen.corpuscles.Simulation;

public class Neuron extends Corpuscle {

    public Neuron(Simulation simulation) {
        super(simulation);
        if(!(simulation instanceof  LocallyConnectedNeuralNetSimulation)){
            throw new RuntimeException("Cannot create this without a " + LocallyConnectedNeuralNetSimulation.class.getSimpleName());
        }
    }
}
