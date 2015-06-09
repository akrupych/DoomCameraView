package akrupych.doomcameraview;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private Direction mDirection = Direction.NORTH;
    private Point mLocation = new Point();

    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // don't recreate fragment after rotation
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        mTextView = (TextView) view.findViewById(R.id.text_view);
        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeBottom() {
                Log.d(TAG, "onSwipeBottom");
                goForward();
            }
            @Override
            public void onSwipeTop() {
                Log.d(TAG, "onSwipeTop");
                goBackward();
            }
            @Override
            public void onSwipeLeft() {
                Log.d(TAG, "onSwipeLeft");
                turnRight();
            }
            @Override
            public void onSwipeRight() {
                Log.d(TAG, "onSwipeRight");
                turnLeft();
            }
        });
        updateView();
    }

    private void goForward() {
        switch (mDirection) {
            case EAST: mLocation.x++; break;
            case NORTH: mLocation.y++; break;
            case SOUTH: mLocation.y--; break;
            case WEST: mLocation.x--; break;
        }
        updateView();
    }

    private void goBackward() {
        switch (mDirection) {
            case EAST: mLocation.x--; break;
            case NORTH: mLocation.y--; break;
            case SOUTH: mLocation.y++; break;
            case WEST: mLocation.x++; break;
        }
        updateView();
    }

    private void turnLeft() {
        switch (mDirection) {
            case EAST: mDirection = Direction.NORTH; break;
            case NORTH: mDirection = Direction.WEST; break;
            case SOUTH: mDirection = Direction.EAST; break;
            case WEST: mDirection = Direction.SOUTH; break;
        }
        updateView();
    }

    private void turnRight() {
        switch (mDirection) {
            case EAST: mDirection = Direction.SOUTH; break;
            case NORTH: mDirection = Direction.EAST; break;
            case SOUTH: mDirection = Direction.WEST; break;
            case WEST: mDirection = Direction.NORTH; break;
        }
        updateView();
    }

    private void updateView() {
        mTextView.setText(String.format("%s (%d;%d)", mDirection.toString(), mLocation.x, mLocation.y));
    }
}
