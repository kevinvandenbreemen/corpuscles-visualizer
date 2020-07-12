package com.vandenbreemen.corpuscles.corpuscles.automaton;

import com.vandenbreemen.corpuscles.CorpusclesData;
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

}
