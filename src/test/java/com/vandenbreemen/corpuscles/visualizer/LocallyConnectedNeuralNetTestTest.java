package com.vandenbreemen.corpuscles.visualizer;

import org.junit.Test;

import static com.vandenbreemen.corpuscles.visualizer.LocallyConnectedNeuralNet.ConnectionDirection.UPPER_RIGHT;
import static org.junit.Assert.*;

public class LocallyConnectedNeuralNetTestTest {

    @Test
    public void testSetConnectionStrength() {
        LocallyConnectedNeuralNet net = new LocallyConnectedNeuralNet(10,10);
        net.setStrength(1,2, UPPER_RIGHT, (byte)10);
        assertEquals(10, net.strength(1,2,UPPER_RIGHT));
    }

}