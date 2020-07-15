package com.vandenbreemen.corpuscles.visualizer;

import org.junit.Test;

import static com.vandenbreemen.corpuscles.visualizer.LocallyConnectedNeuralNet.ConnectionDirection.*;
import static org.junit.Assert.*;

public class LocallyConnectedNeuralNetTestTest {

    @Test
    public void testSetConnectionStrength() {
        LocallyConnectedNeuralNet net = new LocallyConnectedNeuralNet(10,10);
        net.setStrength(1,2, UPPER_RIGHT, (byte)10);
        assertEquals(10, net.strength(1,2,UPPER_RIGHT));
    }

    @Test
    public void testGetDirectionFromOrigin() {
        LocallyConnectedNeuralNet net = new LocallyConnectedNeuralNet(10,10);
        assertEquals(LEFT, net.getDirectionFrom(1,2,1,1));
        assertEquals(RIGHT, net.getDirectionFrom(1,2,1,3));
        assertEquals(ABOVE, net.getDirectionFrom(1,2,2,2));
        assertEquals(BELOW, net.getDirectionFrom(1,2,0,2));
        assertEquals(UPPER_LEFT, net.getDirectionFrom(1,2,2,1));
        assertEquals(UPPER_RIGHT, net.getDirectionFrom(1,2,2,3));
        assertEquals(LOWER_LEFT, net.getDirectionFrom(1,2,0,1));
        assertEquals(LOWER_RIGHT, net.getDirectionFrom(1,2,0,3));

    }

}