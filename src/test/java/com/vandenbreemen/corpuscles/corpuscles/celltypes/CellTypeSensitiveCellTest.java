package com.vandenbreemen.corpuscles.corpuscles.celltypes;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.corpuscles.CellTypes;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellTypeSensitiveCellTest {



    @Test
    public void testCouplingSideBySideActivatedAfterFirstThreeBitsTurnedOn() {

        CorpusclesData cells = new CorpusclesData(10,10);
        CorpusclesData cellTypes = new CorpusclesData(10,10);

        cellTypes.setBit(1,2, CellTypes.COUPLER, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.HORIZONTAL.position, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.CouplerActivations.FIRST_3, true);
        CellTypeSensitiveSimulation cellTypesSim = new CellTypeSensitiveSimulation(cells, cellTypes);

        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(cellTypesSim);
        cellTypesSim.activate(1,1);
        cellTypesSim.activate(1,3);

        //  Activate the first three first

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        //  Now I should be on
        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertTrue(cellTypesSim.activated(1,2));


    }

}