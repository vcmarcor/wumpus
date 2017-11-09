package example.vcmarcor.huntthewumpus.activity.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import example.vcmarcor.huntthewumpus.R;
import example.vcmarcor.huntthewumpus.core.GameMap;

/**
 * Created by victor on 7/11/17.
 * This is the GridView adapter for the map.
 */
public class MapAdapter extends ArrayAdapter<ImageView> {
    
    /**
     * The activity that created the adapter.
     */
    private Activity activity;
    
    /**
     * The map cells grid. True if the cell was discovered, false if not.
     */
    private boolean[][] discoveredCells;
    
    /**
     * The current player position.
     */
    private Point playerPosition;
    
    /**
     * onstructor of the class.
     * @param activity The activity that created the adapter.
     * @param discoveredCells The map cells grid.
     * @param playerPosition The current player position.
     */
    public MapAdapter(final Activity activity, final boolean[][] discoveredCells, final Point playerPosition) {
        super(activity.getApplicationContext(), 0);
        this.activity = activity;
        this.discoveredCells = discoveredCells;
        this.playerPosition = playerPosition;
    }
    
    @Override
    public int getCount() {
        return discoveredCells.length * discoveredCells[0].length;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView mapCell;
        if(convertView == null) {
            // Set the ImageView size as big as the map cell size
            mapCell = new ImageView(activity);
            int cellSize = activity.getResources().getDimensionPixelSize(R.dimen.mini_map_cell_size);
            mapCell.setLayoutParams(new GridView.LayoutParams(cellSize, cellSize));
        }
        else {
            // Recycle the convertView if is not null
            mapCell = (ImageView)convertView;
        }
    
        // Calculate coordinates
        int x = position % discoveredCells.length;
        int y = (int)(position / discoveredCells.length);
        
        // This cell is the current player position
        if(playerPosition.equals(new Point(x, y))) {
            mapCell.setImageResource(R.color.map_player_position);
            return mapCell;
        }
        
        // This cell is the exit
        if(GameMap.STARTING_POSITION.equals(new Point(x, y))) {
            mapCell.setImageResource(R.color.map_exit_position);
            return mapCell;
        }
        
        if(discoveredCells[x][y]) {
            // The cell was discovered
            mapCell.setImageResource(R.color.map_discovered_cell);
        }
        else {
            // The cell wasn't discovered
            mapCell.setImageResource(R.color.map_not_discovered_cell);
        }
    
        return mapCell;
    }
}
