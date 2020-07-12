package com.vandenbreemen.corpuscles.corpuscles.automaton;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.corpuscles.celltypes.CellTypeSensitiveSimulation;

public class CellTypesTester {

    public double testSolution(CorpusclesData cellTypes) {

        CorpusclesData data = new CorpusclesData(cellTypes.height(), cellTypes.width());
        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(data, cellTypes);
        CellTypeSensitiveAutomaton automaton = new CellTypeSensitiveAutomaton(simulation);

        //  Introduce test data
        simulation.activate(1, 0);
        simulation.activate(3, 0);
        simulation.activate(5, 0);
        simulation.activate(7, 0);

        //  Start the next epoch
        simulation.nextEpoch();

        //  Ensure 4,5 on
        automaton.performNextEpoch();
        automaton.performNextEpoch();
        automaton.performNextEpoch();
        automaton.performNextEpoch();

        //

        return 0.0;

    }

    public double computeCost(CorpusclesData data, int[] coordinatesOfCellsToBeActivated) {
        if(coordinatesOfCellsToBeActivated == null || coordinatesOfCellsToBeActivated.length % 2 != 0) {
            throw new RuntimeException("coordinatesOfCellsToBeActivated:  Must be array of ordered coordinate pairs");
        }

        int countWrong = 0;
        int alongHeight;
        int alongWidth;
        for(int i=0; i<coordinatesOfCellsToBeActivated.length; i+=2) {
            alongHeight = coordinatesOfCellsToBeActivated[i];
            alongWidth = coordinatesOfCellsToBeActivated[i+1];

            if(!data.activated(alongHeight, alongWidth)) {
                countWrong ++;
            }
        }

        return (double)countWrong / (double)(coordinatesOfCellsToBeActivated.length/2);
    }
}
