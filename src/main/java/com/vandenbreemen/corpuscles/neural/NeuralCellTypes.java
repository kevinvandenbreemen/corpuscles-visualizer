package com.vandenbreemen.corpuscles.neural;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.visualizer.LocallyConnectedNeuralNet;
import com.vandenbreemen.corpuscles.visualizer.LocallyConnectedNeuralNetSimulation;

import static com.vandenbreemen.corpuscles.Simulation.NeighbourHoodRange.*;

/**
 * Parser/interpreter of neural cell types in order to perform actions for specific cell
 */
public class NeuralCellTypes {

    public static class NeuralGenes {
        /**
         * Bit position to signal that when cell takes turn it must increase connection strengths for any
         * adjacent active cells
         */
        public static final int FIRE_TOGETHER_WIRE_TOGETHER = 2;
    }

    private LocallyConnectedNeuralNetSimulation simulation;
    private CorpusclesData cellTypes;

    NeuralCellTypes(LocallyConnectedNeuralNetSimulation simulation) {
        this.simulation = simulation;
    }

    void performAction(int alongHeight, int alongWidth) {

        int[] neighbourhood = simulation.getMooreNeighbourhoodRange(alongHeight, alongWidth);

        boolean isFireTogetherWireTogether =    //  Fire together wire together logic (only if cur cell is on)
                cellTypes.bitIsOn(alongHeight, alongWidth, NeuralGenes.FIRE_TOGETHER_WIRE_TOGETHER)
                        && simulation.activated(alongHeight, alongWidth);

        for(int h=neighbourhood[HEIGHT_MIN]; h<=neighbourhood[HEIGHT_MAX]; h++) {
            for(int w = neighbourhood[WIDTH_MIN]; w<=neighbourhood[WIDTH_MAX]; w++) {
                if(h == alongHeight && w == alongWidth) continue;

                if(isFireTogetherWireTogether && simulation.activated(h,w)) {
                    LocallyConnectedNeuralNet.ConnectionDirection direction =  LocallyConnectedNeuralNet.getDirectionFrom(alongHeight, alongWidth, h,w);
                    byte strength = simulation.strength(alongHeight, alongWidth, direction);
                    strength += 1;
                    simulation.setStrength(alongHeight, alongWidth, direction, strength);
                }
            }
        }

    }
}
