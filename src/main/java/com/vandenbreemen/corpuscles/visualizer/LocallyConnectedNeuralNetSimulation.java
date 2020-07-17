package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.Simulation;
import com.vandenbreemen.corpuscles.celltypes.CellTypesBuilder;

public class LocallyConnectedNeuralNetSimulation extends Simulation {

    //  Same reference as data in supertype but saves on having to check typecasts etc.
    private LocallyConnectedNeuralNet physicalNetwork;

    //  The same reference as data copy but saves on having to check typecasts etc.
    private LocallyConnectedNeuralNet neuralNetScratchpad;

    /**
     * Genes for the cells
     */
    private CorpusclesData neuralCellTypes;

    public LocallyConnectedNeuralNetSimulation(LocallyConnectedNeuralNet data, CorpusclesData neuralCellTypes) {
        super(data);
        physicalNetwork = data;
        this.neuralCellTypes = neuralCellTypes != null ? neuralCellTypes :
                new CellTypesBuilder().getCellTypes(data.height(), data.width());
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

    /**
     * Gets the cell types associated with this simulation
     * @return
     */
    public CorpusclesData getCellTypes() {
        return neuralCellTypes;
    }
}
