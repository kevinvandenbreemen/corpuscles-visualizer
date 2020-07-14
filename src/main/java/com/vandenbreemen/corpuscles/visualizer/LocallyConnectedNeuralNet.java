package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.CorpusclesData;

/**
 * Network of cells that are connected on all sides.  This is like a neural network, except all connections are
 * width adjacent cells.
 */
public class LocallyConnectedNeuralNet extends CorpusclesData {




    public enum ConnectionDirection {
        UPPER_RIGHT,
        UPPER_LEFT,
        LOWER_RIGHT,
        LOWER_LEFT,
        ABOVE,
        BELOW,
        LEFT,
        RIGHT
    }

    private byte[][] toCellAbove;
    private byte[][] toCellBelow;
    private byte[][] toRightCell;
    private byte[][] toLeftCell;
    private byte[][] toUpperRightCell;
    private byte[][] toUpperLeftCell;
    private byte[][] toLowerRightCell;
    private byte[][] toLowerLeftCell;

    public LocallyConnectedNeuralNet(int height, int width) {
        super(height, width);

        toCellAbove = new byte[height][width];
        toCellBelow = new byte[height][width];
        toLeftCell = new byte[height][width];
        toRightCell = new byte[height][width];
        toUpperLeftCell = new byte[height][width];
        toUpperRightCell = new byte[height][width];
        toLowerLeftCell = new byte[height][width];
        toLowerRightCell = new byte[height][width];
    }

    public void setStrength(int alongHeight, int alongWidth, ConnectionDirection direction, byte strength) {
        getAxonForDirection(direction)[alongHeight][alongWidth] = strength;
    }

    public byte strength(int alongHeight, int alongWidth, ConnectionDirection direction) {
        return getAxonForDirection(direction) [alongHeight][alongWidth];
    }

    private byte[][] getAxonForDirection(ConnectionDirection direction) {
        byte[][] axons;
        switch (direction) {
            case LEFT:
                axons = toLeftCell;
                break;
            case ABOVE:
                axons = toCellAbove;
                break;
            case BELOW:
                axons = toCellBelow;
                break;
            case RIGHT:
                axons = toRightCell;
                break;
            case LOWER_LEFT:
                axons = toLowerLeftCell;
                break;
            case UPPER_LEFT:
                axons = toUpperLeftCell;
                break;
            case LOWER_RIGHT:
                axons = toLowerRightCell;
                break;
            case UPPER_RIGHT:
                axons = toUpperRightCell;
                break;
            default:
                throw new RuntimeException("No set of axons for " + direction);
        }
        return axons;
    }
}
