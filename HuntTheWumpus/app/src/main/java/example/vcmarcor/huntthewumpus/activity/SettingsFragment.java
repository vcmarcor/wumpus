package example.vcmarcor.huntthewumpus.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import example.vcmarcor.huntthewumpus.R;
import example.vcmarcor.huntthewumpus.core.GameMap;

/**
 * Created by victor on 3/11/17.
 * This fragment contains all game settings.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    
    /**
     * MapActivity width preference.
     */
    public static final String PREFERENCE_MAP_WIDTH = "mapWidth";
    
    /**
     * MapActivity height preference.
     */
    public static final String PREFERENCE_MAP_HEIGHT = "mapHeight";
    
    /**
     * Number of arrows preference.
     */
    public static final String PREFERENCE_ARROWS = "arrows";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Add preferences from the preferences resource
        addPreferencesFromResource(R.xml.preferences);
        
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        
        // Set the preference change listener to this class
        Preference preferenceWidth = preferenceScreen.findPreference(PREFERENCE_MAP_WIDTH);
        preferenceWidth.setOnPreferenceChangeListener(this);
    
        Preference preferenceHeight = preferenceScreen.findPreference(PREFERENCE_MAP_HEIGHT);
        preferenceHeight.setOnPreferenceChangeListener(this);
    
        Preference preferenceArrows = preferenceScreen.findPreference(PREFERENCE_ARROWS);
        preferenceArrows.setOnPreferenceChangeListener(this);
    }
    
    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        // Validate the value of the changed preference.
        switch (preference.getKey()) {
            case PREFERENCE_MAP_WIDTH: {
                try {
                    // Check the map width, it can't be less than the minimum
                    int value = Integer.parseInt(o.toString());
                    if(value < GameMap.MIN_WIDTH) {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.toast_map_width, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    return true;
                }catch(NumberFormatException nfe) { return false; }
            }
    
            case PREFERENCE_MAP_HEIGHT: {
                try {
                    // Check the map height, it can't be less than the minimum
                    int value = Integer.parseInt(o.toString());
                    if(value < GameMap.MIN_HEIGHT) {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.toast_map_height, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    return true;
                }catch(NumberFormatException nfe) { return false; }
            }
            
            case PREFERENCE_ARROWS: {
                return true;
            }
        }
        return false;
    }
}
