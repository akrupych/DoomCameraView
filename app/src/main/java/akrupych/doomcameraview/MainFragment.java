package akrupych.doomcameraview;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private MovementController mController = new MovementController();

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
            public void onSwipe(SwipeDirection direction) {
                Log.d(TAG, "onSwipe " + direction);
                switch (direction) {
                    case BOTTOM: mController.goForward(); break;
                    case TOP: mController.goBackward(); break;
                    case LEFT: mController.turnRight(); break;
                    case RIGHT: mController.turnLeft(); break;
                }
                updateView();
            }
        });
        updateView();
    }

    private void updateView() {
        mTextView.setText(mController.toString());
    }
}
