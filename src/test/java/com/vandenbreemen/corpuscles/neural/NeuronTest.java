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
    public void testConnectionStrengthDecreasedIfFireTogetherWireTogetherCurrentCellNotFiringButOtherCellIs() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);

        //  Connection strength increment value
        cellTypes.writeData(1,2, (byte)2, 3, 4);
        cellTypes.setBit(1,2, NeuralCellTypes.NeuralGenes.FIRE_TOGETHER_WIRE_TOGETHER, true);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);
        simulation.activate(1,1);
        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.LEFT, (byte)2);
        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertEquals(
                "Adjacent cell was firing while current cell was not.  Connection strength should have decremented",
                0, simulation.strength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.LEFT));
    }

    @Test
    public void testConnectionStrengthDecreasedIfFireTogetherWireTogetherCurrentCellFiringButOtherCellIsNot() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);

        //  Connection strength increment value
        cellTypes.writeData(1,2, (byte)2, 3, 4);
        cellTypes.setBit(1,2, NeuralCellTypes.NeuralGenes.FIRE_TOGETHER_WIRE_TOGETHER, true);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);
        simulation.activate(1,2);
        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.LEFT, (byte)2);
        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertEquals(
                "Current cell is firing but adjacent cell is not.  Connection strength should have decremented",
                0, simulation.strength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.LEFT));
    }

    @Test
    public void testConnectStrengthDropsTo0IfTooManyDecreases() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);

        //  Connection strength increment value
        cellTypes.writeData(1,2, (byte)2, 3, 4);
        cellTypes.setBit(1,2, NeuralCellTypes.NeuralGenes.FIRE_TOGETHER_WIRE_TOGETHER, true);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);
        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.LEFT, (byte)100);
        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);

        for(int i=0; i<1000; i++) {

            //  Artificially simulate cell active so we can test strength decrease
            simulation.activate(1,2);
            simulation.nextEpoch();

            neuron.takeTurn(1, 2);
            simulation.nextEpoch();
        }

        assertEquals(
                "Current cell is firing but adjacent cell is not.  Connection strength should have decremented",
                0, simulation.strength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.LEFT));
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
    public void testCellDoesNotActivateIfNoThresholdOrActivation() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 2, true);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);

        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertFalse("No incoming activations.  Cell should not be firing", simulation.activated(1,2));
    }

    @Test
    public void testIncomingSignalStrengthBasedOnlyOnAdjacentFiringCells() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 2, true);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);

        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.LEFT, (byte)5);
        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.RIGHT, (byte)5);

        simulation.activate(1,1);

        //  Set activation threshold
        simulation.writeData(1,2, (byte)50, 1, 7);

        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertFalse("Firing threshold not met.  This should not be active",
                simulation.activated(1,2));
    }

    @Test
    public void testIncomingSignalStrengthSummationOfFiringCellsPushOverThresholdToFire() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 2, true);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);

        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.LEFT, (byte)5);
        simulation.setStrength(1,2, LocallyConnectedNeuralNet.ConnectionDirection.RIGHT, (byte)5);

        simulation.activate(1,1);
        simulation.activate(1,3);

        //  Set activation threshold
        simulation.writeData(1,2, (byte)50, 1, 7);

        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertTrue("Firing threshold met.  Cell should fire.",
                simulation.activated(1,2));
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

    @Test
    public void testFireBelowThreshold() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 1, true); //  Fire below a certain threshold
        cellTypes.writeData(1,2, Byte.MAX_VALUE, 3, 7);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);

        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertTrue("Min threshold not met.  This cell should be firing", simulation.activated(1,2));


    }

    @Test
    public void testInhibitoryNeuronActivation() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 0, true); //  Inhibitory Neuron

        //  Fraction of cells required to get inhibitory cell to fire - 1/4 of all cells
        cellTypes.writeData(1,2, (byte)0, 5,6);

        //  radius of cells to check for firing - 1 cell out
        cellTypes.writeData(1,2, (byte)0, 3, 4);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);

        simulation.activate(1,1);
        simulation.activate(1,3);
        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertTrue("One quarter of cells in radius of 1 cell active.  Inhibitory cell should be active",
                simulation.activated(1,2));
    }

    @Test
    public void testInhibitoryNeuronMinActivationThresholdNotMet() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,2, 0, true); //  Inhibitory Neuron

        //  Fraction of cells required to get inhibitory cell to fire - 1/2 of all cells
        cellTypes.writeData(1,2, (byte)1, 5,6);

        //  radius of cells to check for firing - 1 cell out
        cellTypes.writeData(1,2, (byte)0, 3, 4);

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);

        simulation.activate(1,1);
        simulation.activate(1,2);
        simulation.activate(1,3);
        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertFalse("One half of cells in radius of 1 cell must active.  Only 1 quarter were.  Cell should be off.",
                simulation.activated(1,2));
    }

    @Test
    public void testReceiveSignalFromInhibitoryNeuron() {
        CorpusclesData cellTypes = new CorpusclesData(10,10);
        cellTypes.setBit(1,3, 0, true); //  Inhibitory Neuron
        cellTypes.setBit(1,3, 7, true); //  Increase activation threshold by 10

        LocallyConnectedNeuralNet network = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(network, cellTypes);
        simulation.writeData(1,2,(byte)1, 1, 7);  //  Activation threshold
        simulation.activate(1,3);   //  Inhibitory neuron is running

        simulation.nextEpoch();

        Neuron neuron = new Neuron(simulation);
        neuron.takeTurn(1,2);
        simulation.nextEpoch();

        assertEquals(11, simulation.data(1,2, 1, 7));
    }

}
