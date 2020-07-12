package com.vandenbreemen.corpuscles.corpuscles.celltypes;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.corpuscles.automaton.CellTypesTester;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class CellTypesTesterTest {

    @Test
    public void testComputesCostFunctionForCorrectAnswer() {
        CellTypesTester tester = new CellTypesTester();
        CorpusclesData data = new CorpusclesData(3,3);
        data.activate(0,2);
        data.activate(2,2);

        //  True/false/true at the far right side
        int[] expectedActivations = {
                0,2,
                2,2
        };

        double cost = tester.computeCost(data, expectedActivations);
        assertEquals(0.0, cost);
    }

    @Test
    public void testComputesCostFunctionForIncorrectAnswer() {
        CellTypesTester tester = new CellTypesTester();
        CorpusclesData data = new CorpusclesData(3,3);

        //  True/false/true at the far right side
        int[] expectedActivations = {
                0,2,
                2,2
        };

        double cost = tester.computeCost(data, expectedActivations);
        assertEquals(1.0, cost);
    }

}
