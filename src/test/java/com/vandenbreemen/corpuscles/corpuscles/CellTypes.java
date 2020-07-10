package com.vandenbreemen.corpuscles.corpuscles;

public class CellTypes {

    /**
     * Coupler byte position.  Coupler cells connect cells to each other
     */
    public static final int COUPLER = 0;

    private CellTypes(){}

    public enum CouplerTypes {

        HORIZONTAL(1),
        VERTICAL(2),
        FWD_SLASH(3)
        ;

        public final int position;

        private CouplerTypes(int position) {
            this.position = position;
        }

        public static class CouplerActivations {
            public static final int FIRST_3 = 4;
        }

    }
}
