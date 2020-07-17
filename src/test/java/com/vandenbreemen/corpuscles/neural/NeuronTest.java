package com.vandenbreemen.corpuscles.neural;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.visualizer.LocallyConnectedNeuralNet;
import com.vandenbreemen.corpuscles.visualizer.LocallyConnectedNeuralNetSimulation;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class NeuronTest {

    @Test
    public void testConnectionStrengthIncreasesWhenAdjacentNeuronFiring() {

        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 2, true);

        //  Connection strength increment value
        cellTypes.writeData(1,2, (byte)2, 3, 4);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);
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
    public void testConnectionStrengthNotChangedIfFireTogetherWireTogetherNotOn() {

        CorpusclesData cellTypes = new CorpusclesData(10,10);

        //  Connection strength increment value
        cellTypes.writeData(1,2, (byte)2, 3, 4);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);
        simulation.activate(1,1);
        simulation.activate(1,2);
        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertEquals(0, simulation.strength(1,2,
                LocallyConnectedNeuralNet.ConnectionDirection.LEFT));
    }

    @Test
    public void testMaximumConnectionStrength() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 2, true);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);
        simulation.activate(1,1);
        simulation.activate(1,2);
        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.LEFT, Byte.MAX_VALUE);
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

    @Test
    public void testSigmoidFiring() {

        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 2, true);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);

        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.LEFT, Byte.MAX_VALUE);
        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.RIGHT, Byte.MAX_VALUE);

        simulation.activate(1,1);
        simulation.activate(1,3);

        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertTrue(simulation.activated(1,2));

    }

    @Test
    public void testReadActivationThresholdFromCellData() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 2, true);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);

        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.LEFT, (byte)120);
        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.RIGHT, Byte.MAX_VALUE);

        simulation.activate(1,1);
        simulation.activate(1,3);

        //  Set activation threshold
        simulation.writeData(1,2, Byte.MAX_VALUE, 1, 7);

        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertFalse(simulation.activated(1,2));
    }
    
    @Test
    public void testStrengthStepIncrementConfiguration() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 2, true);

        //  Connection strength increment value
        cellTypes.writeData(1,2, (byte)2, 3, 4);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);

        simulation.activate(1,1);
        simulation.activate(1,3);
        simulation.activate(1,2);

        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertEquals(2, simulation.strength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.RIGHT));
    }

}
