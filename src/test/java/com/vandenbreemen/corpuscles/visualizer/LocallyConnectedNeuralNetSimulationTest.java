package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.neural.LocallyConnectedNeuralNet;
import com.vandenbreemen.corpuscles.neural.LocallyConnectedNeuralNetSimulation;
import org.junit.Test;

import static com.vandenbreemen.corpuscles.neural.LocallyConnectedNeuralNet.ConnectionDirection.*;
import static org.junit.Assert.assertEquals;

public class LocallyConnectedNeuralNetSimulationTest {

    @Test
    public void testPreservesConnectionsAfterEpoch() {
        LocallyConnectedNeuralNet net = new LocallyConnectedNeuralNet(10,10);
        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(net, null);

        simulation.setStrength(1,2, UPPER_RIGHT, (byte)1);
        simulation.setStrength(1,2, UPPER_LEFT, (byte)2);
        simulation.setStrength(1,2, LOWER_LEFT, (byte)3);
        simulation.setStrength(1,2, LOWER_RIGHT, (byte)4);
        simulation.setStrength(1,2, LEFT, (byte)5);
        simulation.setStrength(1,2, RIGHT, (byte)6);
        simulation.setStrength(1,2, ABOVE, (byte)7);
        simulation.setStrength(1,2, BELOW, (byte)8);
        simulation.nextEpoch();

        assertEquals(1, simulation.strength(1,2,UPPER_RIGHT));
        assertEquals(2, simulation.strength(1,2,UPPER_LEFT));
        assertEquals(3, simulation.strength(1,2,LOWER_LEFT));
        assertEquals(4, simulation.strength(1,2,LOWER_RIGHT));
        assertEquals(5, simulation.strength(1,2,LEFT));
        assertEquals(6, simulation.strength(1,2,RIGHT));
        assertEquals(7, simulation.strength(1,2,ABOVE));
        assertEquals(8, simulation.strength(1,2,BELOW));

    }

}