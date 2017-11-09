package example.vcmarcor.huntthewumpus;

import android.graphics.Point;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import example.vcmarcor.huntthewumpus.core.GameMap;
import example.vcmarcor.huntthewumpus.core.MapCell;

import static org.junit.Assert.*;
/**
 * Created by victor on 28/10/17.
 */

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GameMapTest {
    
    private final int MAP_WIDTH = 10;
    private final int MAP_HEIGHT = 10;
    
    @Test
    public void generateGoldTest() throws Exception {
        GameMap gameMap = new GameMap(MAP_WIDTH, MAP_HEIGHT);
        Point goldPoint = gameMap.getGoldPoint();
        
        // Gold point should be different from the starting point
        assertFalse(GameMap.STARTING_POSITION.equals(goldPoint));
        
        // The map cell on the position of the gold point should be of type GOLD
        assertEquals(MapCell.CellType.GOLD, gameMap.getMapCells()[goldPoint.x][goldPoint.y].getType());
    }
    
    @Test
    public void generateWumpusTest() throws Exception {
        GameMap gameMap = new GameMap(MAP_WIDTH, MAP_HEIGHT);
        Point wumpusPoint = gameMap.getWunmpusPoint();
        
        // The Wumpus can't be located at the starting point
        assertFalse(wumpusPoint.equals(GameMap.STARTING_POSITION));
        
        // The Wumpus can't be located at the gold point
        assertFalse(wumpusPoint.equals(gameMap.getGoldPoint()));
        
        // The map cell on the position of the Wumpus point should be of type WUMPUS
        assertEquals(MapCell.CellType.WUMPUS, gameMap.getMapCells()[wumpusPoint.x][wumpusPoint.y].getType());
    }
}
