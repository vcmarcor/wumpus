package example.vcmarcor.huntthewumpus.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import example.vcmarcor.huntthewumpus.R;
import example.vcmarcor.huntthewumpus.activity.adapter.MiniMapAdapter;
import example.vcmarcor.huntthewumpus.activity.adapter.MiniMapAdapter.MapCellDiscovered;
import example.vcmarcor.huntthewumpus.core.Game;
import example.vcmarcor.huntthewumpus.core.Game.Direction;
import example.vcmarcor.huntthewumpus.core.Game.ShootingResult;
import example.vcmarcor.huntthewumpus.core.MoveResult;
import example.vcmarcor.huntthewumpus.core.MoveResult.Result;

/**
 * Created by victor on 4/11/17.
 *
 * This is the game activity. Here the user controls the game.
 */
public class GameActivity extends Activity implements OnClickListener{
    
    /**
     * Extra containing the map width.
     */
    public static final String EXTRA_WIDTH = "width";
    
    /**
     * Extra containing the map height.
     */
    public static final String EXTRA_HEIGHT = "height";
    
    /**
     * Extras containing the number of arrows.
     */
    public static final String EXTRA_ARROWS = "arrows";
    
    /**
     * The received map width.
     */
    private int mapWidth;
    
    /**
     * The received map height.
     */
    private int mapHeight;
    
    /**
     * The received number of arrows.
     */
    private int arrows;
    
    /**
     * The current game.
     */
    private Game game;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    
        // Validate the intent before continue
        Bundle extras = getIntent().getExtras();
        if(!validateIntent(extras)) {
            finish();
        }
        
        // Extract all extras
        mapWidth = extras.getInt(EXTRA_WIDTH);
        mapHeight = extras.getInt(EXTRA_HEIGHT);
        arrows = extras.getInt(EXTRA_ARROWS);
        
        // Initialize all controls
        Button showMap = findViewById(R.id.showMap);
        showMap.setOnClickListener(this);
        Button moveUp = findViewById(R.id.moveUp);
        moveUp.setOnClickListener(this);
        Button moveDown = findViewById(R.id.moveDown);
        moveDown.setOnClickListener(this);
        Button moveLeft = findViewById(R.id.moveLeft);
        moveLeft.setOnClickListener(this);
        Button moveRight = findViewById(R.id.moveRight);
        moveRight.setOnClickListener(this);
        Button shootUp = findViewById(R.id.shootUp);
        shootUp.setOnClickListener(this);
        Button shootDown = findViewById(R.id.shootDown);
        shootDown.setOnClickListener(this);
        Button shootLeft = findViewById(R.id.shootLeft);
        shootLeft.setOnClickListener(this);
        Button shootRight = findViewById(R.id.shootRight);
        shootRight.setOnClickListener(this);
        
        // Start new game
        startNewGame();
    }
    
    /**
     * Starts a new game with the options from the extras.
     */
    private void startNewGame() {
        game = new Game(mapWidth, mapHeight, arrows);
        updateMinimap(game.getPerceptions());
        
        // Update everything
        updateArrowsLeft();
        updateTotalMovements();
        updateNeededMovements();
    }
    
    /**
     * Validates that the intent has all necessary extras to start a game.
     * @param extras The Bundle extras from the intent.
     * @return True if is valid, false if not.
     */
    private boolean validateIntent(final Bundle extras) {
        if(extras == null) {
            return false;
        }
        
        return extras.containsKey(EXTRA_WIDTH) && extras.containsKey(EXTRA_HEIGHT) && extras.containsKey(EXTRA_ARROWS);
    }
    
    /**
     * Updates the minimap based on the current game status.
     * @param moveResult The MoveResult containing the perceptions of the player.
     */
    private void updateMinimap(MoveResult moveResult) {
        // Update perceptions to be invisible
        TextView perceptionWell = findViewById(R.id.perceptionWell);
        perceptionWell.setVisibility(View.INVISIBLE);
        TextView perceptionGold = findViewById(R.id.perceptionGold);
        perceptionGold.setVisibility(View.INVISIBLE);
        TextView perceptionWumpus = findViewById(R.id.perceptionWumpus);
        perceptionWumpus.setVisibility(View.INVISIBLE);
        
        // Make visible only the active perceptions
        if(moveResult.isPerceivingWell()) {
            perceptionWell.setVisibility(View.VISIBLE);
        }
        
        if(moveResult.isPerceivingGold()) {
            perceptionGold.setVisibility(View.VISIBLE);
        }
        
        if(moveResult.isPerceivingWumpus()) {
            perceptionWumpus.setVisibility(View.VISIBLE);
        }
    
        // Setup the GridView containing the mini map
        GridView miniMapGridView = findViewById(R.id.miniMapGridView);
        miniMapGridView.setAdapter(new MiniMapAdapter(this, getSurroundingDiscoveredMapCells()));
        // Set the width to be 3 cells large
        LayoutParams gridviewParams = miniMapGridView.getLayoutParams();
        gridviewParams.width = getResources().getDimensionPixelSize(R.dimen.mini_map_cell_size) * 3;
        gridviewParams.width += getResources().getDimensionPixelSize(R.dimen.mini_map_cells_spacing) * 2;
        miniMapGridView.setLayoutParams(gridviewParams);
    }
    
    /**
     * Updates the total movements
     */
    private void updateTotalMovements() {
        TextView totalMovements = findViewById(R.id.totalMovements);
        totalMovements.setText(String.valueOf(game.getMovements()));
    }
    
    /**
     * Updates the needed movements to beat the game.
     */
    private void updateNeededMovements() {
        TextView neededMovements = findViewById(R.id.neededMovements);
        neededMovements.setText(String.valueOf(game.getSolutionLength()));
    }
    
    /**
     * Updates the number of arrows left.
     */
    private void updateArrowsLeft() {
        TextView arrowsLeft = findViewById(R.id.arrowsLeft);
        arrowsLeft.setText(String.valueOf(String.valueOf(game.getArrows())));
    }
    
    /**
     * Gets a 3x3 array of MapCellDiscovered. Those will be the surroundings of the player,
     * player will be on the center. This is used to show on the mini map the discovered cells.
     * @return A 3x3 array of MapCellDiscovered where the player is on the center.
     */
    private MapCellDiscovered[][] getSurroundingDiscoveredMapCells() {
        Point playerPosition = game.getPlayerPosition();
        boolean[][] discoveredCells = game.getDiscoveredCells();
        MapCellDiscovered[][] result = new MapCellDiscovered[3][3];
        
        // Start filling the result array
        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++ ) {
                // Start from the player position with an offset on -1,-1
                Point currentPoint = new Point(playerPosition);
                currentPoint.offset(i - 1, j - 1);
                
                // Check if the point is outside the map
                if(!game.isPointIsideMap(currentPoint)) {
                    result[i][j] = MapCellDiscovered.OUT_OF_MAP;
                    continue;
                }
                
                // Check if it is the exit
                if(game.getStartingPosition().equals(currentPoint)) {
                    result[i][j] = MapCellDiscovered.EXIT;
                    continue;
                }
                
                // Check if the cell was discovered or not
                if(discoveredCells[currentPoint.x][currentPoint.y]) {
                    result[i][j] = MapCellDiscovered.DISCOVERED;
                }
                else{
                    result[i][j] = MapCellDiscovered.NOT_DISCOVERED;
                }
            }
        }
        
        return result;
    }
    
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.showMap: {
                // The show map button
                showMap();
                break;
            }
            
            case R.id.moveUp: {
                // The move up button
                movePlayer(Direction.UP);
                break;
            }
            
            case R.id.moveDown: {
                // The move down button
                movePlayer(Direction.DOWN);
                break;
            }
    
            case R.id.moveLeft: {
                // The move left button
                movePlayer(Direction.LEFT);
                break;
            }
    
            case R.id.moveRight: {
                // The move right button
                movePlayer(Direction.RIGHT);
                break;
            }
    
            case R.id.shootUp: {
                // The shoot up button
                shootArrow(Direction.UP);
                break;
            }
    
            case R.id.shootDown: {
                // The shoot down button
                shootArrow(Direction.DOWN);
                break;
            }
    
            case R.id.shootLeft: {
                // The shoot left button
                shootArrow(Direction.LEFT);
                break;
            }
    
            case R.id.shootRight: {
                // The shoot right button
                shootArrow(Direction.RIGHT);
                break;
            }
        }
    }
    
    /**
     * Starts the activity which shows the full map.
     */
    private void showMap() {
        Intent mapIntent = new Intent(this, MapActivity.class);
        mapIntent.putExtra(MapActivity.EXTRA_DISCOVERED_CELLS, game.getDiscoveredCells());
        mapIntent.putExtra(MapActivity.EXTRA_PLAYER_POSITION, game.getPlayerPosition());
        startActivity(mapIntent);
    }
    
    /**
     * Move the player in the given direction.
     * This method will update the mini map and the total movements.
     * @param direction The direction to move the player.
     */
    private void movePlayer(final Direction direction) {
        // Move the player
        MoveResult moveResult = game.movePlayer(direction);
        
        // Check the result of the movement
        switch(moveResult.getResult()) {
            case DEAD:
            case DIED_WELL:
            case DIED_WUMPUS:
            case WIN: {
                // The player died, alert the user
                alertGameFinished(moveResult.getResult());
                break;
            }
            
            case GOLD: {
                // The player obtained the gold, alert the user
                showSimpleAlertDialog(R.string.congratulations, R.string.gold_obtained);
                break;
            }
            
            case HIT_WALL: {
                // The arrow hit the wall, show a quick message
                Toast.makeText(this, R.string.toast_hit_wall, Toast.LENGTH_SHORT).show();
                break;
            }
            
            case ERROR: {
                // This should never happen, show some error message in case it happens
                Toast.makeText(this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
                break;
            }
        }
        
        // Update mini map and total movements
        updateMinimap(moveResult);
        updateTotalMovements();
    }
    
    /**
     * Shoot an arrow in the given direction.
     * This method will update the arrows left.
     * @param direction the direction to shoot.
     */
    private void shootArrow(final Direction direction) {
        // Shoot the arrow
        ShootingResult shootingResult = game.shootArrow(direction);
        
        // Check the shooting result
        switch(shootingResult) {
            case DEAD: {
                // The player is dead, alert the user (this should not happen)
                alertGameFinished(Result.DEAD);
                break;
            }
            
            case WUMPUS_KILLED:
            case MISSED: {
                // If the Wumpus was killed, or the arrow missed, alert the user
                alertShootResult(shootingResult);
                break;
            }
            
            case NO_ARROWS: {
                // The player has no arrows left, show a quick message
                Toast.makeText(this, R.string.toast_no_arrows, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    
        // Update the arrows left
        updateArrowsLeft();
    }
    
    /**
     * Alerts to the user that the game has finished.
     * It can be due to the player is dead, or won the game.
     * @param result The Result of the game.
     */
    private void alertGameFinished(final Result result) {
        // Initialize with game over
        int title = R.string.game_over;
        int message = R.string.already_dead;
        
        // Check the result
        switch(result) {
            case DIED_WELL: {
                // The player fell into a well
                message = R.string.died_well;
                break;
            }
            
            case DIED_WUMPUS: {
                // The Wumpus killed the player
                message = R.string.died_wumpus;
                break;
            }
            
            case WIN: {
                // The player won the game
                title = R.string.congratulations;
                message = R.string.win_game;
                break;
            }
        }
        
        // Create a new alert dialog with the title and message set, ask for a new game
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.new_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startNewGame();
            }
        });
        builder.setNegativeButton(R.string.finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
    
    /**
     * Alerts the user about a shooting result.
     * It can be a missed shoot, or a killing shoot.
     * @param shootingResult The shooting result.
     */
    private void alertShootResult(final ShootingResult shootingResult) {
        // Initialize with the missed message
        int title = R.string.shoot_missed_title;
        int message = R.string.shoot_missed;
        
        // Check if the player killed the Wumpus to change the message
        if(shootingResult == ShootingResult.WUMPUS_KILLED) {
            title = R.string.congratulations;
            message = R.string.shoot_killed;
        }
    
        // Show the alert dialog
        showSimpleAlertDialog(title, message);
    }
    
    /**
     * Shows an alert dialog with the given title and message.
     * @param title The resource ID referring the title.
     * @param message The resource ID referring the message.
     */
    private void showSimpleAlertDialog(final int title, final int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }
}
