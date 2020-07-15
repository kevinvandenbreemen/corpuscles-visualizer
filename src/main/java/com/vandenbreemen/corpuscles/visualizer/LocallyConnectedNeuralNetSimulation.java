package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.Simulation;

public class LocallyConnectedNeuralNetSimulation extends Simulation {

    //  Same reference as data in supertype but saves on having to check typecasts etc.
    private LocallyConnectedNeuralNet physicalNetwork;

    //  The same reference as data copy but saves on having to check typecasts etc.
    private LocallyConnectedNeuralNet neuralNetScratchpad;

    public LocallyConnectedNeuralNetSimulation(CorpusclesData data) {
        super(data);
        if (!(data instanceof LocallyConnectedNeuralNet) ){
            throw new RuntimeException("Cannot create this without " + LocallyConnectedNeuralNet.class.getSimpleName());
        }
        physicalNetwork = (LocallyConnectedNeuralNet) data;
    }

    @Override
    protected CorpusclesData buildDataCopy(int height, int width) {
        neuralNetScratchpad = new LocallyConnectedNeuralNet(height, width);
        return neuralNetScratchpad;
    }

    public void setStrength(int alongHeight, int alongWidth, LocallyConnectedNeuralNet.ConnectionDirection direction, byte strength) {
        neuralNetScratchpad.setStrength(alongHeight, alongWidth, direction, strength);
    }

    public byte strength(int alongHeight, int alongWidth, LocallyConnectedNeuralNet.ConnectionDirection direction) {
        return physicalNetwork.strength(alongHeight, alongWidth, direction);
    }
}
