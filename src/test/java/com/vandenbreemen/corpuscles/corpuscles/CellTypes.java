package com.vandenbreemen.corpuscles.corpuscles;

public class CellTypes {

    /**
     * Coupler byte position.  Coupler cells connect cells to each other
     */
    public static final int COUPLER = 0;

    /**
     * Cell that receives activation from a coupler cell when that coupler is on
     */
    public static final int COUPLER_ENDPOINT = 1;
    public static final int INHIBITOR = 2;

    private CellTypes(){}

    public enum CouplerTypes {

        HORIZONTAL(4),
        VERTICAL(5),
        FWD_SLASH(6)
        ;

        public final int position;

        private CouplerTypes(int position) {
            this.position = position;
        }

        public static class CouplerActivations {
            public static final int FIRST_3 = 7;
        }

    }

    public enum InhibitorTypes {

        TwoCells(4),
        FourCells(5),

        ;

        public final int position;

        private InhibitorTypes(int position) {
            this.position = position;
        }

    }
}
