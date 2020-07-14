package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.Simulation;

public class LocallyConnectedNeuralNetSimulation extends Simulation {

    public LocallyConnectedNeuralNetSimulation(CorpusclesData data) {
        super(data);
        if (!(data instanceof LocallyConnectedNeuralNet) ){
            throw new RuntimeException("Cannot create this without " + LocallyConnectedNeuralNet.class.getSimpleName());
        }
    }
}
