package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.CellularAutomaton;
import com.vandenbreemen.corpuscles.Corpuscle;
import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.Simulation;
import com.vandenbreemen.corpuscles.celltypes.CellTypesBuilder;
import com.vandenbreemen.corpuscles.neural.LocallyConnectedNeuralNet;
import com.vandenbreemen.corpuscles.neural.LocallyConnectedNeuralNetSimulation;
import com.vandenbreemen.corpuscles.neural.Neuron;

/**
 * Testing class to see what the neural net looks like / behaves like
 */
public class WorkInProgressVisualization {



    public static void main(String[] args) {
        LocallyConnectedNeuralNet net = new LocallyConnectedNeuralNet(10, 10);

        CellTypesBuilder bld = new CellTypesBuilder();
        CorpusclesData cellTypes = bld.getCellTypes(10, 10);

        LocallyConnectedNeuralNetSimulation simulation = new LocallyConnectedNeuralNetSimulation(net, cellTypes);

        final Neuron neuron = new Neuron(simulation);
        CorpusclesVisualizer visualizer = new CorpusclesVisualizer(simulation, new CellularAutomaton(simulation) {
            @Override
            protected Corpuscle getCorpuscle(int alongWidth, int alongHeight, Simulation simulation) {
                return neuron;
            }
        }, new CellRenderer());
        visualizer.setCellClickListener((alongHeight, alongWidth) -> {
            if(simulation.activated(alongHeight, alongWidth)) {
                simulation.deactivate(alongHeight, alongWidth);
                return;
            }
            simulation.activate(alongHeight, alongWidth);
            simulation.nextEpoch();
            visualizer.redraw();
        });

    }

}
