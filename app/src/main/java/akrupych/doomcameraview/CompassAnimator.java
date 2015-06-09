package akrupych.doomcameraview;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Handles compass image rotation.
 * Usage: call {@link #setup(ImageView, MovementController)} to set initial rotation and
 * {@link #rotateLeft(ImageView)} or {@link #rotateRight(ImageView)} for the next ones
 */
public class CompassAnimator {

    private int mDegrees;

    public void setup(ImageView compassView, MovementController controller) {
        switch (controller.getDirection()) {
            case NORTH: mDegrees = 0; break;
            case EAST: mDegrees = 90; break;
            case SOUTH: mDegrees = 180; break;
            case WEST: mDegrees = 270; break;
        }
        startAnimation(compassView, 0, mDegrees);
    }

    public void rotateLeft(ImageView compassView) {
        startAnimation(compassView, mDegrees, mDegrees - 90);
    }

    public void rotateRight(ImageView compassView) {
        startAnimation(compassView, mDegrees, mDegrees + 90);
    }

    private void startAnimation(ImageView imageView, int fromDegrees, int toDegrees) {
        mDegrees = toDegrees;
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
    }
}
