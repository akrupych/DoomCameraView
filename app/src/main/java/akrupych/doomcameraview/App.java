package akrupych.doomcameraview;

import android.app.Application;

/**
 * Context source for the whole application. Usage: singleton.
 * Application has to have public default constructor, so it's left untouched.
 */
public class App extends Application {

    private static App sInstance;

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
