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

    byte[][] toCellAbove;
    byte[][] toCellBelow;
    byte[][] toRightCell;
    byte[][] toLeftCell;
    byte[][] toUpperRightCell;
    byte[][] toUpperLeftCell;
    byte[][] toLowerRightCell;
    byte[][] toLowerLeftCell;

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

    @Override
    protected void overwriteWith(CorpusclesData data) {
        if(!(data instanceof  LocallyConnectedNeuralNet)) {
            throw new RuntimeException("Overwrite with - cannot use data that is not a " + LocallyConnectedNeuralNet.class.getSimpleName());
        }

        LocallyConnectedNeuralNet otherNet = (LocallyConnectedNeuralNet)data;

        super.overwriteWith(data);

        for (int i = 0; i < this.height(); i++) {
            System.arraycopy(otherNet.toCellAbove[i], 0, toCellAbove[i], 0, width());
            System.arraycopy(otherNet.toCellBelow[i], 0, toCellBelow[i], 0, width());
            System.arraycopy(otherNet.toLeftCell[i], 0, toLeftCell[i], 0, width());
            System.arraycopy(otherNet.toRightCell[i], 0, toRightCell[i], 0, width());
            System.arraycopy(otherNet.toUpperLeftCell[i], 0, toUpperLeftCell[i], 0, width());
            System.arraycopy(otherNet.toUpperRightCell[i], 0, toUpperRightCell[i], 0, width());
            System.arraycopy(otherNet.toLowerLeftCell[i], 0, toLowerLeftCell[i], 0, width());
            System.arraycopy(otherNet.toLowerRightCell[i], 0, toLowerRightCell[i], 0, width());
        }

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

    public static ConnectionDirection getDirectionFrom(int originAlngHeight, int originAlngWidth, int destAlngHeight, int destAlngWidth) {
        if(destAlngWidth < originAlngWidth) {   //  To the left
            if(destAlngHeight == originAlngHeight) {
                return ConnectionDirection.LEFT;
            } else if(destAlngHeight > originAlngHeight) {
                return ConnectionDirection.UPPER_LEFT;
            } else if(destAlngHeight < originAlngHeight) {
                return ConnectionDirection.LOWER_LEFT;
            }
        }
        if(destAlngWidth > originAlngWidth) {   //  To the right
            if(destAlngHeight == originAlngHeight) {
                return ConnectionDirection.RIGHT;
            } else if(destAlngHeight > originAlngHeight) {
                return ConnectionDirection.UPPER_RIGHT;
            } else if(destAlngHeight < originAlngHeight) {
                return ConnectionDirection.LOWER_RIGHT;
            }

        } else {
            if(destAlngHeight > originAlngHeight) {
                return ConnectionDirection.ABOVE;
            }
            else {
                if(destAlngHeight < originAlngHeight) {
                    return ConnectionDirection.BELOW;
                }
            }
        }
        return null;
    }
}
