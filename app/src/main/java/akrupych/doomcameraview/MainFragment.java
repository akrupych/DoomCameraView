package akrupych.doomcameraview;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private MovementController mController = new MovementController();
    private CompassAnimator mCompassAnimator = new CompassAnimator();

    private PhotoView mPhotoView;
    private ImageView mCompassView;
    private Button mTakePhotoButton;
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
        mPhotoView = (PhotoView) view.findViewById(R.id.photo);
        mCompassView = (ImageView) view.findViewById(R.id.compass);
        mCompassAnimator.setup(mCompassView, mController);
        mTextView = (TextView) view.findViewById(R.id.location);
        mTakePhotoButton = (Button) view.findViewById(R.id.take_photo);
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipe(SwipeDirection direction) {
                Log.d(TAG, "onSwipe " + direction);
                switch (direction) {
                    case BOTTOM: mController.goForward(); break;
                    case TOP: mController.goBackward(); break;
                    case LEFT:
                        mController.turnRight();
                        mCompassAnimator.rotateRight(mCompassView);
                        break;
                    case RIGHT:
                        mController.turnLeft();
                        mCompassAnimator.rotateLeft(mCompassView);
                        break;
                }
                updateView();
            }
        });
        updateView();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPhotoFile()));
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @NonNull
    private File getPhotoFile() {
        return new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), mController.toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) updateView();
    }

    private void updateView() {
        File photoFile = getPhotoFile();
        boolean photoAvailable = photoFile.exists();
        mPhotoView.setImageFile(photoFile);
        mTakePhotoButton.setVisibility(photoAvailable ? View.GONE : View.VISIBLE);
        mTextView.setText(mController.getLocationString());
    }
}
