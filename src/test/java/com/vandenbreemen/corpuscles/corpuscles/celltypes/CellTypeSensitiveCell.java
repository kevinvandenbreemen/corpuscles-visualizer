package com.vandenbreemen.corpuscles.corpuscles.celltypes;

import com.vandenbreemen.corpuscles.Corpuscle;
import com.vandenbreemen.corpuscles.Simulation;
import com.vandenbreemen.corpuscles.corpuscles.CellTypes;

import static com.vandenbreemen.corpuscles.Simulation.NeighbourHoodRange.*;

/**
 * Cell that is aware of its own cell type
 */
public class CellTypeSensitiveCell extends Corpuscle {

    private CellTypeSensitiveSimulation cellTypeSimulation() {
        return (CellTypeSensitiveSimulation)simulation;
    }

    public CellTypeSensitiveCell(Simulation simulation) {
        super(simulation);
        if (!(simulation instanceof CellTypeSensitiveSimulation)){
            throw new RuntimeException("Cannot create this kind of cell without a " + CellTypeSensitiveSimulation.class.getSimpleName());
        }
    }

    @Override
    public void takeTurn(int alongHeight, int alongWidth) {

        int[] neightbourhood = simulation.getMooreNeighbourhoodRange(alongHeight, alongWidth);
        Simulation cellTypes = cellTypeSimulation().cellTypes;
        boolean isCouplingCell = cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.COUPLER);


        if(isCouplingCell) {

            //  Step 1:  Identify the cells we're coupling
            int[] cellCoordOne;
            int[] cellCoordTwo;

            if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.HORIZONTAL.position)) {
                cellCoordOne = new int[]{alongHeight, neightbourhood[WIDTH_MIN]};
                cellCoordTwo = new int[]{alongHeight, neightbourhood[WIDTH_MAX]};
            } else if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.VERTICAL.position)) {
                cellCoordOne = new int[]{neightbourhood[HEIGHT_MIN], alongWidth};
                cellCoordTwo = new int[]{neightbourhood[HEIGHT_MAX], alongWidth};
            } else if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.FWD_SLASH.position)) {
                cellCoordOne = new int[]{neightbourhood[HEIGHT_MIN], neightbourhood[WIDTH_MIN]};
                cellCoordTwo = new int[]{neightbourhood[HEIGHT_MAX], neightbourhood[WIDTH_MAX]};
            } else /*Backslash*/ {
                cellCoordOne = new int[]{neightbourhood[HEIGHT_MAX], neightbourhood[WIDTH_MIN]};
                cellCoordTwo = new int[]{neightbourhood[HEIGHT_MIN], neightbourhood[WIDTH_MAX]};
            }

            //  Step 2:  Check the states of these cells
            boolean cell1On = simulation.activated(cellCoordOne[0], cellCoordOne[1]) || simulation.activated(cellCoordOne[1], cellCoordOne[0]);
            boolean cell2On = simulation.activated(cellCoordTwo[0], cellCoordTwo[1]) || simulation.activated(cellCoordTwo[1], cellCoordTwo[0]);

            //  Step 3:  Determine what to do with activations
            if(cell1On && cell2On) {

                //  Do I just want to turn on my first three slots?
                if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.CouplerActivations.FIRST_3)) {

                    //  Step 3a:  Are all my bits turned on?
                    if(simulation.bitIsOn(alongHeight, alongWidth, 1) &&
                            simulation.bitIsOn(alongHeight, alongWidth, 2) &&
                            simulation.bitIsOn(alongHeight, alongWidth, 3)) {
                        simulation.activate(alongHeight, alongWidth);
                    }
                    else {
                        if(!simulation.bitIsOn(alongHeight, alongWidth, 1)) {
                            simulation.setBit(alongHeight, alongWidth, 1, true);
                        }
                        else if(!simulation.bitIsOn(alongHeight, alongWidth, 2)) {
                            simulation.setBit(alongHeight, alongWidth, 2, true);
                        }
                        else if(!simulation.bitIsOn(alongHeight, alongWidth, 3)) {
                            simulation.setBit(alongHeight, alongWidth, 3, true);
                        }
                    }
                }

            }

        }

    }
}
