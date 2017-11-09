package example.vcmarcor.huntthewumpus.activity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.GridView;

import example.vcmarcor.huntthewumpus.R;
import example.vcmarcor.huntthewumpus.activity.adapter.MapAdapter;

/**
 * Created by victor on 7/11/17.
 * This activity shows the current map. It shows the player position, the exit position,
 * the discovered cells, and the not discovered cells.
 */
public class MapActivity extends Activity {
    
    /**
     * Extra containing the discovered cells array.
     */
    public static final String EXTRA_DISCOVERED_CELLS = "discovered_cells";
    
    /**
     * Extra containing the current player position.
     */
    public static final String EXTRA_PLAYER_POSITION = "player_position";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    
        // Validate the intent before continue
        Bundle extras = getIntent().getExtras();
        if(!validateIntent(extras)) {
            finish();
        }
    
        // Extract all extras
        boolean[][] discoveredCells = (boolean[][])extras.get(EXTRA_DISCOVERED_CELLS);
        Point playerPosition = (Point)extras.get(EXTRA_PLAYER_POSITION);
    
        // Setup the GridView containing the map
        GridView mapGridView = findViewById(R.id.mapGridView);
        mapGridView.setNumColumns(discoveredCells.length);
        // Set the width of the map to contain all cells
        ViewGroup.LayoutParams gridviewParams = mapGridView.getLayoutParams();
        gridviewParams.width = getResources().getDimensionPixelSize(R.dimen.map_cell_size) * discoveredCells.length;
        gridviewParams.width += getResources().getDimensionPixelSize(R.dimen.map_cells_spacing) * (discoveredCells.length - 1);
        gridviewParams.width += getResources().getDimensionPixelSize(R.dimen.map_margin) * 2;
        mapGridView.setLayoutParams(gridviewParams);
        mapGridView.setAdapter(new MapAdapter(this, discoveredCells, playerPosition));
    }
    
    /**
     * Validates that the intent has all necessary extras to show the map.
     * @param extras The Bundle extras from the intent.
     * @return True if is valid, false if not.
     */
    private boolean validateIntent(final Bundle extras) {
        if(extras == null) {
            return false;
        }
        
        return extras.containsKey(EXTRA_DISCOVERED_CELLS) && extras.containsKey(EXTRA_PLAYER_POSITION);
    }
}
