package akrupych.doomcameraview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * Loads scaled down bitmaps onto view
 */
public class PhotoView extends ImageView {

    private static final String TAG = PhotoView.class.getSimpleName();

    private File mImageFile;
    private boolean mSkipMeasures = false;

    public PhotoView(Context context) {
        super(context);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mSkipMeasures) tryShowImage();
    }

    public void setImageFile(File file) {
        mImageFile = file;
        tryShowImage();
    }

    private void tryShowImage() {
        if (mImageFile == null || getWidth() == 0 || getHeight() == 0) return;
        mSkipMeasures = true;
        String photoPath = mImageFile.getAbsolutePath();
        int photoOrientation = getPhotoOrientation(photoPath);
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoWidth = bmOptions.outWidth;
        int photoHeight = bmOptions.outHeight;
        if (shouldSwitchSides(photoOrientation)) { // swap them
            photoHeight = photoHeight + photoWidth;
            photoWidth = photoHeight - photoWidth;
            photoHeight = photoHeight - photoWidth;
        }
        // Determine how much to scale down the image
        int scaleFactor = Math.max(photoWidth / getWidth(), photoHeight / getHeight());
        Log.d(TAG, String.format("scaling %dx%d image to %dx%d view with factor %d",
                photoWidth, photoHeight, getWidth(), getHeight(), scaleFactor));
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        Bitmap scaledBitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        setImageBitmap(rotateBitmap(scaledBitmap, photoOrientation));
    }

    private int getPhotoOrientation(String photoPath) {
        try {
            ExifInterface exif = new ExifInterface(photoPath);
            return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        } catch (IOException e) {
            e.printStackTrace();
            return ExifInterface.ORIENTATION_UNDEFINED;
        }
    }

    private boolean shouldSwitchSides(int photoOrientation) {
        switch (photoOrientation) {
            case ExifInterface.ORIENTATION_TRANSPOSE:
            case ExifInterface.ORIENTATION_ROTATE_90:
            case ExifInterface.ORIENTATION_TRANSVERSE:
            case ExifInterface.ORIENTATION_ROTATE_270: return true;
            default: return false;
        }
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}
