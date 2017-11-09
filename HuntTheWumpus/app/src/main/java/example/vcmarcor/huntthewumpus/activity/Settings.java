package example.vcmarcor.huntthewumpus.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by victor on 3/11/17.
 * This activity is a container for the settings fragment.
 */
public class Settings extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        // Create a new settings fragment and show it
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
