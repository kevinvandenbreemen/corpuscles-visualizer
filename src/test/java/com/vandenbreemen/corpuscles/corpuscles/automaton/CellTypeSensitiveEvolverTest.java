package com.vandenbreemen.corpuscles.corpuscles.automaton;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.corpuscles.celltypes.CellTypeSensitiveCell;
import com.vandenbreemen.corpuscles.corpuscles.celltypes.CellTypeSensitiveSimulation;
import com.vandenbreemen.corpuscles.corpuscles.visualizer.CellTypeSensitiveRenderer;
import com.vandenbreemen.corpuscles.visualizer.CorpusclesVisualizer;
import org.junit.Test;

public class CellTypeSensitiveEvolverTest {

    @Test
    public void testEvolution() {

        //  Define a tester
        int[] expectedActivations = { 0,1, 1,1};
        int[] inputs = {0,0};
        CellTypesTester tester = new CellTypesTester();

        CellTypeSensitiveEvolver evolver = new CellTypeSensitiveEvolver(new CellTypesMutator(), new CellTypesBuilder(), 2, 2) {
            @Override
            protected double testSolution(CorpusclesData cellTypes) {
                return tester.testSolution(cellTypes, 2, inputs, expectedActivations);
            }
        };
        evolver.prepareSolutions(); //  Generate genepool
        evolver.testSolutions();

    }

    @Test
    public void testEvolutionOfSolutions() {
        CellTypesProcreator procreator = new CellTypesProcreator();
        int height = 10;
        int width = 10;

        //  Define a tester
        int[] expectedActivations = { 0,9, 1,9, 2,9, 3,9};
        int[] inputs = {0,0, 3,0};
        CellTypesTester tester = new CellTypesTester();

        CellTypeSensitiveEvolver evolver = new CellTypeSensitiveEvolver(new CellTypesMutator(), new CellTypesBuilder(), height, width) {
            @Override
            protected double testSolution(CorpusclesData cellTypes) {
                return tester.testSolution(cellTypes, 3, inputs, expectedActivations);
            }
        };
        evolver.prepareSolutions(); //  Generate genepool
        evolver.testSolutions();

        for(int i=0; i<200; i++) {
            //  Remove the last 3
            evolver.cellTypesSets.remove(7);
            evolver.cellTypesSets.remove(7);
            evolver.cellTypesSets.remove(7);

            evolver.cellTypesSets.add(procreator.getNextSolution(evolver.cellTypesSets, new CellTypesMutator()));
            evolver.cellTypesSets.add(procreator.getNextSolution(evolver.cellTypesSets, new CellTypesMutator()));
            evolver.cellTypesSets.add(procreator.getNextSolution(evolver.cellTypesSets, new CellTypesMutator()));

            evolver.testSolutions();
        }

        CorpusclesData optimalSolution = evolver.bestSolution();
        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(new CorpusclesData(height,width), optimalSolution);
        CellTypeSensitiveRenderer renderer = new CellTypeSensitiveRenderer(new CellTypeSensitiveCell(simulation));
        CorpusclesVisualizer visualizer = new CorpusclesVisualizer(simulation, new CellTypeSensitiveAutomaton(simulation), renderer);

        try {
            Thread.sleep(10000);
        }catch(Exception ex) {}

    }

    @Test
    public void testDisplaySolution() {

        int height = 10;
        int width = 10;

        //  Define a tester
        int[] expectedActivations = { 0,9, 1,9, 2,9, 3,9};
        int[] inputs = {0,0, 3,0};
        CellTypesTester tester = new CellTypesTester();

        CellTypeSensitiveEvolver evolver = new CellTypeSensitiveEvolver(new CellTypesMutator(), new CellTypesBuilder(), height, width) {
            @Override
            protected double testSolution(CorpusclesData cellTypes) {
                return tester.testSolution(cellTypes, 3, inputs, expectedActivations);
            }
        };
        evolver.prepareSolutions(); //  Generate genepool
        evolver.testSolutions();

        CorpusclesData optimalSolution = evolver.bestSolution();
        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(new CorpusclesData(height,width), optimalSolution);
        CellTypeSensitiveRenderer renderer = new CellTypeSensitiveRenderer(new CellTypeSensitiveCell(simulation));
        CorpusclesVisualizer visualizer = new CorpusclesVisualizer(simulation, new CellTypeSensitiveAutomaton(simulation), renderer);

        try {
            Thread.sleep(10000);
        }catch(Exception ex) {}
    }

}
