package example.vcmarcor.huntthewumpus.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import example.vcmarcor.huntthewumpus.R;

/**
 * Created by victor on 4/11/17.
 * This is the GridView adapter for the mini map. It is designet to be a 3x3 map, if the given map
 * is of a different size, this adapter won't work at all.
 */
public class MiniMapAdapter extends ArrayAdapter<ImageView> {
    
    /**
     * Represents a cell status, it can be:<br>
     * -DISCOVERED (The cell was discovered)<br>
     * -NOT_DISCOVERED (The cell wasn't discovered)<br>
     * -EXIT (The cell contains the exit)<br>
     * -OUT_OF_MAP (The cell is out of the map)
     */
    public enum MapCellDiscovered {DISCOVERED, NOT_DISCOVERED, EXIT, OUT_OF_MAP};
    
    /**
     * The activity that created the adapter.
     */
    private Activity activity;
    
    /**
     * The map cells grid.
     */
    private MapCellDiscovered[][] mapCells;
    
    /**
     * Constructor of the class.
     * @param activity The activity that created the adapter.
     * @param mapCells The map cells grid.
     */
    public MiniMapAdapter(final Activity activity, final MapCellDiscovered[][] mapCells) {
        super(activity.getApplicationContext(), 0);
        this.activity = activity;
        this.mapCells = mapCells;
    }
    
    @Override
    public int getCount() {
        return mapCells.length * mapCells[0].length;
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
        
        // This is the center of the minimap (player position)
        if(position == 4) {
            mapCell.setImageResource(R.color.minimap_player_position);
            return mapCell;
        }
        
        // Calculate coordinates
        int x = position % mapCells.length;
        int y = position / mapCells.length;
        
        switch(mapCells[x][y]) {
            case EXIT: {
                // This cell is the exit
                mapCell.setImageResource(R.color.minimap_exit_position);
                break;
            }
            
            case DISCOVERED: {
                // This cell was discovered
                mapCell.setImageResource(R.color.minimap_discovered_cell);
                break;
            }
            
            case NOT_DISCOVERED: {
                // This cell wasn't discovered
                mapCell.setImageResource(R.color.minimap_not_discovered_cell);
                break;
            }
            
            case OUT_OF_MAP: {
                // This cell is out of themap
                mapCell.setImageResource(R.color.minimap_background);
                break;
            }
        }
        
        return mapCell;
    }
}
