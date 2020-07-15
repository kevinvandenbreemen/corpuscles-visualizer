package com.vandenbreemen.corpuscles.neural;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.visualizer.LocallyConnectedNeuralNet;
import com.vandenbreemen.corpuscles.visualizer.LocallyConnectedNeuralNetSimulation;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class NeuronTest {

    @Test
    public void testConnectionStrengthIncreasesWhenAdjacentNeuronFiring() {

        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 2, true);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network);
        simulation.activate(1,1);
        simulation.activate(1,2);
        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertTrue(simulation.strength(1,2,
                LocallyConnectedNeuralNet.ConnectionDirection.LEFT) > 0);
    }

    @Test
    public void testMaximumConnectionStrength() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 2, true);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network);
        simulation.activate(1,1);
        simulation.activate(1,2);
        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);

        //  Get this as strongly connected as possible!
        for(int i=0; i<100000; i++) {
            neuron.takeTurn(1, 2);
            simulation.nextEpoch();
        }

        assertEquals(Byte.MAX_VALUE, simulation.strength(1,2,
                LocallyConnectedNeuralNet.ConnectionDirection.LEFT));
    }

}
