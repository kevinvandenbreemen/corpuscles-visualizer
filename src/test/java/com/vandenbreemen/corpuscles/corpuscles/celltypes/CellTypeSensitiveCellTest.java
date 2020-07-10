package com.vandenbreemen.corpuscles.corpuscles.celltypes;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.corpuscles.CellTypes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellTypeSensitiveCellTest {

    private CorpusclesData cells;
    private CorpusclesData cellTypes;

    @Before
    public void setup() {
        cells = new CorpusclesData(10,10);
        cellTypes = new CorpusclesData(10,10);
    }


    @Test
    public void testCouplingSideBySideActivatedAfterFirstThreeBitsTurnedOn() {

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

    @Test
    public void testDetectsWhenAdjacentCellIsCouplerAndIsTurnedOn() {

        //  Set up coupler cell
        cellTypes.setBit(1,2, CellTypes.COUPLER, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.VERTICAL.position, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.CouplerActivations.FIRST_3, true);

        //  Set up coupler endpoint
        cellTypes.setBit(2, 2, CellTypes.COUPLER_ENDPOINT, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        //  Turn on the coupler
        simulation.activate(1,2);
        simulation.nextEpoch();


        //  Now execute turn for endpoint.  Endpoint should be turned on
        assertFalse(cellTypes.activated(2,2));

        cell.takeTurn(2,2);
        simulation.nextEpoch();

        assertTrue(simulation.activated(2,2));

    }

    @Test
    public void testDetectsWhenAdjacentCellIsCouplerEndpointCellDoesNotActivateIfCouplerNotActivated() {

        //  Set up coupler cell
        cellTypes.setBit(1,2, CellTypes.COUPLER, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.VERTICAL.position, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.CouplerActivations.FIRST_3, true);

        //  Set up coupler endpoint
        cellTypes.setBit(2, 2, CellTypes.COUPLER_ENDPOINT, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        //  Leave coupler off
        simulation.nextEpoch();


        //  Now execute turn for endpoint.  Endpoint should be turned on
        assertFalse(cellTypes.activated(2,2));

        cell.takeTurn(2,2);
        simulation.nextEpoch();

        assertFalse(simulation.activated(2,2));

    }

}