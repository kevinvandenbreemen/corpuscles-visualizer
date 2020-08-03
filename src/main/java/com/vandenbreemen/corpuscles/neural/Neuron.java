package com.vandenbreemen.corpuscles.neural;

import com.vandenbreemen.corpuscles.Corpuscle;
import com.vandenbreemen.corpuscles.Simulation;

public class Neuron extends Corpuscle {

    /**
     * Handles actual actions
     */
    private NeuralCellTypes neuralCellTypes;

    public Neuron(Simulation simulation) {
        super(simulation);
        if(!(simulation instanceof LocallyConnectedNeuralNetSimulation)){
            throw new RuntimeException("Cannot create this without a " + LocallyConnectedNeuralNetSimulation.class.getSimpleName());
        }

        this.neuralCellTypes = new NeuralCellTypes((LocallyConnectedNeuralNetSimulation) simulation);
    }

    @Override
    public void takeTurn(int alongHeight, int alongWidth) {
        neuralCellTypes.performAction(alongHeight, alongWidth);
    }
}
