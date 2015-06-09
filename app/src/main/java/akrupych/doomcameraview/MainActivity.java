package akrupych.doomcameraview;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) { // the first run
            // fragment will be created only once
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_holder, new MainFragment())
                    .commit();
        }
    }
}
