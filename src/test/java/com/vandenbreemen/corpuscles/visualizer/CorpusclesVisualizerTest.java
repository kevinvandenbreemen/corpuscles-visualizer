package com.vandenbreemen.corpuscles.visualizer;


import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.Simulation;

public class CorpusclesVisualizerTest {

    public static void main(String[] args) {
        CorpusclesData data = new CorpusclesData(10,10);
        Simulation sim = new Simulation(data);

        sim.activate(5,5);
        sim.activate(4,5);
        sim.activate(3,5);
        sim.activate(1,5);
        sim.nextEpoch();

        CorpusclesVisualizer visualizer = new CorpusclesVisualizer(sim);
    }

}