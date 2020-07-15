package com.vandenbreemen.corpuscles.neural;

import com.vandenbreemen.corpuscles.Corpuscle;
import com.vandenbreemen.corpuscles.Simulation;
import com.vandenbreemen.corpuscles.visualizer.LocallyConnectedNeuralNetSimulation;

public class Neuron extends Corpuscle {

    public Neuron(Simulation simulation) {
        super(simulation);
        if(!(simulation instanceof LocallyConnectedNeuralNetSimulation)){
            throw new RuntimeException("Cannot create this without a " + LocallyConnectedNeuralNetSimulation.class.getSimpleName());
        }
    }
}
