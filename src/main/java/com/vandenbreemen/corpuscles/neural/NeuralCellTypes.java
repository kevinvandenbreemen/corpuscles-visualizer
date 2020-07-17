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
        public static final int FIRE_BELOW_THRESHOLD = 1;
    }

    private LocallyConnectedNeuralNetSimulation simulation;
    private CorpusclesData cellTypes;

    NeuralCellTypes(LocallyConnectedNeuralNetSimulation simulation) {
        this.simulation = simulation;
        this.cellTypes = simulation.getCellTypes();
    }

    void performAction(int alongHeight, int alongWidth) {

        int[] neighbourhood = simulation.getMooreNeighbourhoodRange(alongHeight, alongWidth);

        boolean isFireTogetherWireTogether =    //  Fire together wire together logic (only if cur cell is on)
                cellTypes.bitIsOn(alongHeight, alongWidth, NeuralGenes.FIRE_TOGETHER_WIRE_TOGETHER)
                        && simulation.activated(alongHeight, alongWidth);


        //  Now sum up all incoming firing connections
        double totalIncomingStrength = 0;
        double incomingStrength;

        byte strengthIncrement = cellTypes.data(alongHeight, alongWidth, 3, 4);

        for(int h=neighbourhood[HEIGHT_MIN]; h<=neighbourhood[HEIGHT_MAX]; h++) {
            for(int w = neighbourhood[WIDTH_MIN]; w<=neighbourhood[WIDTH_MAX]; w++) {
                if(h == alongHeight && w == alongWidth) continue;

                LocallyConnectedNeuralNet.ConnectionDirection direction =  LocallyConnectedNeuralNet.getDirectionFrom(alongHeight, alongWidth, h,w);

                if(simulation.activated(h,w)) {
                    incomingStrength = (double) simulation.strength(alongHeight, alongWidth, direction);
                    totalIncomingStrength += incomingStrength;
                }

                if(isFireTogetherWireTogether && simulation.activated(h,w)) {
                    byte strength = simulation.strength(alongHeight, alongWidth, direction);

                    if(strength < Byte.MAX_VALUE) {
                        strength += strengthIncrement;
                    }
                    simulation.setStrength(alongHeight, alongWidth, direction, strength);
                } else if(isFireTogetherWireTogether && !simulation.activated(h,w) ){
                    byte strength = simulation.strength(alongHeight, alongWidth, direction);
                    strength -= strengthIncrement;
                    simulation.setStrength(alongHeight, alongWidth, direction, strength);
                } else if(cellTypes.bitIsOn(alongHeight, alongWidth, NeuralGenes.FIRE_TOGETHER_WIRE_TOGETHER) && simulation.activated(h,w)) {
                    byte strength = simulation.strength(alongHeight, alongWidth, direction);
                    strength -= strengthIncrement;
                    simulation.setStrength(alongHeight, alongWidth, direction, strength);
                }

            }
        }

        //  Determine if we should be firing
        totalIncomingStrength /= (double)10;    //  Compress this down to a range from -5 to +5 so sigmoid function will give meaningful results
        double sigmoidValue = sigmoid(totalIncomingStrength);
        if(checkAgainstActivationThreshold(alongHeight, alongWidth, sigmoidValue)) {
            simulation.activate(alongHeight, alongWidth);
        } else {
            simulation.deactivate(alongHeight, alongWidth);
        }

    }

    private boolean checkAgainstActivationThreshold(int alongHeight, int alongWidth, double strength) {

        boolean isFireBelowCertainThreshold = cellTypes.bitIsOn(alongHeight, alongWidth, NeuralGenes.FIRE_BELOW_THRESHOLD);
        if(isFireBelowCertainThreshold) {
            final double thresholdRange = 2.0;
            double thresholdIncrement = thresholdRange / 32.0;  //  32 possible values (2 ^ 5)
            double thresholdValue = (double)cellTypes.data(alongHeight, alongWidth, 3, 7);
            thresholdValue *= thresholdIncrement;
            thresholdValue -= 1.0;  //  (range from -1.0 to 1.0)
            return strength < thresholdValue;
        }

        byte rawThreshold = simulation.data(alongHeight, alongWidth, 1, 7);
        double thresholdDbl = (double) rawThreshold / (double)(Byte.MAX_VALUE);
        return strength >= thresholdDbl;
    }

    private double sigmoid(double value) {
        //  Math function created with the help of https://www.desmos.com/calculator
        //  2\cdot\left(\frac{1}{\left(1+e^{\left(\left(-x\right)\right)}\right)}-0.5\right)
        return 2 * ((1 / (1+ Math.exp(-value))) - 0.5);
    }
}
