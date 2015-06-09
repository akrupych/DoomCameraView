package akrupych.doomcameraview;

import android.graphics.Point;

/**
 * Encapsulates direction and movement in space.
 */
public class MovementController {

    private Direction mDirection = Direction.NORTH;
    private Point mLocation = new Point();

    public void goForward() {
        switch (mDirection) {
            case EAST: mLocation.x++; break;
            case NORTH: mLocation.y++; break;
            case SOUTH: mLocation.y--; break;
            case WEST: mLocation.x--; break;
        }
    }

    public void goBackward() {
        switch (mDirection) {
            case EAST: mLocation.x--; break;
            case NORTH: mLocation.y--; break;
            case SOUTH: mLocation.y++; break;
            case WEST: mLocation.x++; break;
        }
    }

    public void turnLeft() {
        switch (mDirection) {
            case EAST: mDirection = Direction.NORTH; break;
            case NORTH: mDirection = Direction.WEST; break;
            case SOUTH: mDirection = Direction.EAST; break;
            case WEST: mDirection = Direction.SOUTH; break;
        }
    }

    public void turnRight() {
        switch (mDirection) {
            case EAST: mDirection = Direction.SOUTH; break;
            case NORTH: mDirection = Direction.EAST; break;
            case SOUTH: mDirection = Direction.WEST; break;
            case WEST: mDirection = Direction.NORTH; break;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%d;%d)", mDirection.toString(), mLocation.x, mLocation.y);
    }
}
