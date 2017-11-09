package example.vcmarcor.huntthewumpus.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import example.vcmarcor.huntthewumpus.R;
import example.vcmarcor.huntthewumpus.core.GameMap;

/**
 * Created by victor on 3/11/17.
 * This is the main menu activity. Here the user can set up the game settings, and see the
 * guide to learn how to play.
 */
public class MainMenuActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        // Set up the buttons
        Button buttonSettings = findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(this);
        
        Button buttonHowToPlay = findViewById(R.id.buttonHowToPlay);
        buttonHowToPlay.setOnClickListener(this);
        
        Button buttonNewGame = findViewById(R.id.buttonNewGame);
        buttonNewGame.setOnClickListener(this);
    }
    
    /**
     * Starts the activity to change the game settings.
     */
    private void showSettings() {
        startActivity(new Intent(this, Settings.class));
    }
    
    /**
     * Starts the activity to learn how to play the game.
     */
    private void showTutorial() {
        startActivity(new Intent(this, TutorialActivity.class));
    }
    
    /**
     * Starts the activity to start a new game. Creates the new game from the current settings.
     */
    private void statNewGame() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            // Parse current setting, or gte defaults (0 for arrows)
            int mapWidth = Integer.parseInt(sharedPreferences.getString(SettingsFragment.PREFERENCE_MAP_WIDTH, String.valueOf(GameMap.MIN_WIDTH)));
            int mapHeight = Integer.parseInt(sharedPreferences.getString(SettingsFragment.PREFERENCE_MAP_HEIGHT, String.valueOf(GameMap.MIN_HEIGHT)));
            int arrows = Integer.parseInt(sharedPreferences.getString(SettingsFragment.PREFERENCE_ARROWS, "0"));
            
            // Create the new intent and start it
            Intent gameIntent = new Intent(this, GameActivity.class);
            gameIntent.putExtra(GameActivity.EXTRA_WIDTH, mapWidth);
            gameIntent.putExtra(GameActivity.EXTRA_HEIGHT, mapHeight);
            gameIntent.putExtra(GameActivity.EXTRA_ARROWS, arrows);
            startActivity(gameIntent);
        } catch(NumberFormatException nfe) { Toast.makeText(getApplicationContext(), R.string.toast_error_settings, Toast.LENGTH_SHORT).show(); }
    }
    
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonSettings: {
                // Settings button
                showSettings();
                break;
            }
            
            case R.id.buttonHowToPlay: {
                // How to play button
                showTutorial();
                break;
            }
            
            case R.id.buttonNewGame: {
                // New game button
                statNewGame();
                break;
            }
        }
    }
}
