package example.vcmarcor.huntthewumpus;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import example.vcmarcor.huntthewumpus.core.Game;
import example.vcmarcor.huntthewumpus.core.Game.ShootingResult;
import example.vcmarcor.huntthewumpus.core.Game.Direction;
import example.vcmarcor.huntthewumpus.core.MoveResult;
import example.vcmarcor.huntthewumpus.core.MoveResult.Result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by victor on 30/10/17.
 */

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GameTest {
    
    private final int MAP_WIDTH = 10;
    private final int MAP_HEIGHT = 10;
    private final int ARROWS = 5;
    
    @Test
    public void moveOutsideTest() throws Exception {
        Game game = new Game(MAP_WIDTH, MAP_HEIGHT, ARROWS);
        
        // Try to move UP when game starts (player is at 0,0 and should not be able to move)
        MoveResult moveResult = game.movePlayer(Direction.UP);
        assertEquals(Result.HIT_WALL, moveResult.getResult());
    
        // Try to move UP again to check the player couldn't move before
        moveResult = game.movePlayer(Direction.UP);
        assertEquals(Result.HIT_WALL, moveResult.getResult());
    
        // Try to move LEFT, player should remain at 0,0 and shouldn't be able to move
        moveResult = game.movePlayer(Direction.LEFT);
        assertEquals(Result.HIT_WALL, moveResult.getResult());
    }
    
    @Test
    public void shootingUpTest() {
        Game game = new Game(MAP_WIDTH, MAP_HEIGHT, ARROWS);
        
        // Try to shoot UP as many times as possible (should miss always)
        for(int i = 1; i <= ARROWS; i++) {
            ShootingResult shootingResult = game.shootArrow(Direction.UP);
            assertEquals(ShootingResult.MISSED, shootingResult);
        }
        
        // Try to shoot one more arrow, and should't be able (no more arrows)
        ShootingResult shootingResult = game.shootArrow(Direction.UP);
        assertEquals(ShootingResult.NO_ARROWS, shootingResult);
    }
    
    @Test
    public void shootingLeftTest() {
        Game game = new Game(MAP_WIDTH, MAP_HEIGHT, ARROWS);
        
        // Try to shoot LEFT as many times as possible (should miss always)
        for(int i = 1; i <= ARROWS; i++) {
            ShootingResult shootingResult = game.shootArrow(Direction.LEFT);
            assertEquals(ShootingResult.MISSED, shootingResult);
        }
        
        // Try to shoot one more arrow, and should't be able (no more arrows)
        ShootingResult shootingResult = game.shootArrow(Direction.LEFT);
        assertEquals(ShootingResult.NO_ARROWS, shootingResult);
    }
}
