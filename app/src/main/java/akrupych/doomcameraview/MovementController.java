package akrupych.doomcameraview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.Log;

/**
 * Encapsulates direction and movement in space.
 */
public class MovementController {

    private static final String TAG = MovementController.class.getSimpleName();
    private static final String KEY_DIRECTION = "KEY_DIRECTION";
    private static final String KEY_X = "KEY_X";
    private static final String KEY_Y = "KEY_Y";

    private SharedPreferences mSource;
    private Direction mDirection;
    private Point mLocation;

    public MovementController() {
        mSource = App.getInstance().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        mDirection = Direction.valueOf(mSource.getString(KEY_DIRECTION, Direction.NORTH.toString()));
        mLocation = new Point(mSource.getInt(KEY_X, 0), mSource.getInt(KEY_Y, 0));
        Log.d(TAG, "loaded " + toString());
    }

    private void save() {
        mSource.edit().putString(KEY_DIRECTION, mDirection.toString())
                .putInt(KEY_X, mLocation.x).putInt(KEY_Y, mLocation.y).apply();
        Log.d(TAG, "saved " + toString());
    }

    public void goForward() {
        switch (mDirection) {
            case EAST: mLocation.x++; break;
            case NORTH: mLocation.y++; break;
            case SOUTH: mLocation.y--; break;
            case WEST: mLocation.x--; break;
        }
        save();
    }

    public void goBackward() {
        switch (mDirection) {
            case EAST: mLocation.x--; break;
            case NORTH: mLocation.y--; break;
            case SOUTH: mLocation.y++; break;
            case WEST: mLocation.x++; break;
        }
        save();
    }

    public void turnLeft() {
        switch (mDirection) {
            case EAST: mDirection = Direction.NORTH; break;
            case NORTH: mDirection = Direction.WEST; break;
            case SOUTH: mDirection = Direction.EAST; break;
            case WEST: mDirection = Direction.SOUTH; break;
        }
        save();
    }

    public void turnRight() {
        switch (mDirection) {
            case EAST: mDirection = Direction.SOUTH; break;
            case NORTH: mDirection = Direction.EAST; break;
            case SOUTH: mDirection = Direction.WEST; break;
            case WEST: mDirection = Direction.NORTH; break;
        }
        save();
    }

    @Override
    public String toString() {
        return String.format("%s (%d;%d)", mDirection.toString(), mLocation.x, mLocation.y);
    }

    public Direction getDirection() {
        return mDirection;
    }
}
