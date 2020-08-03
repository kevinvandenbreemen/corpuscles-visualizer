package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.neural.LocallyConnectedNeuralNet;
import org.junit.Test;

import static com.vandenbreemen.corpuscles.neural.LocallyConnectedNeuralNet.ConnectionDirection.*;
import static org.junit.Assert.assertEquals;

public class LocallyConnectedNeuralNetTestTest {

    @Test
    public void testSetConnectionStrength() {
        LocallyConnectedNeuralNet net = new LocallyConnectedNeuralNet(10,10);
        net.setStrength(1,2, UPPER_RIGHT, (byte)10);
        assertEquals(10, net.strength(1,2,UPPER_RIGHT));
    }

    @Test
    public void testGetDirectionFromOrigin() {
        assertEquals(LEFT, LocallyConnectedNeuralNet.getDirectionFrom(1,2,1,1));
        assertEquals(RIGHT, LocallyConnectedNeuralNet.getDirectionFrom(1,2,1,3));
        assertEquals(ABOVE, LocallyConnectedNeuralNet.getDirectionFrom(1,2,2,2));
        assertEquals(BELOW, LocallyConnectedNeuralNet.getDirectionFrom(1,2,0,2));
        assertEquals(UPPER_LEFT, LocallyConnectedNeuralNet.getDirectionFrom(1,2,2,1));
        assertEquals(UPPER_RIGHT, LocallyConnectedNeuralNet.getDirectionFrom(1,2,2,3));
        assertEquals(LOWER_LEFT, LocallyConnectedNeuralNet.getDirectionFrom(1,2,0,1));
        assertEquals(LOWER_RIGHT, LocallyConnectedNeuralNet.getDirectionFrom(1,2,0,3));

    }

}