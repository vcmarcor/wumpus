package example.vcmarcor.huntthewumpus.core;

import android.graphics.Point;

import example.vcmarcor.huntthewumpus.core.MoveResult.Result;

/**
 * Created by victor on 28/10/17.<br>
 *
 * This class contains all game logic of the game mechanics.
 */
public class Game {
    /**
     * Direction of movement/shooting, it can be:<br>
     * -UP<br>
     * -DOWN<br>
     * -LEFT<br>
     * -RIGHT
     */
    public enum Direction {UP, DOWN, LEFT, RIGHT};
    
    /**
     * This represents the result of shooting an arrow, it can be:<br>
     * -NO_ARROWS (The player has no arrows left)<br>
     * -MISSED (The shoot missed the Wumpus)<br>
     * -WUMPUS_KILLED (The shoot killed the Wumpus<br>
     * -DEAD (The player is dead, so can't shoot)
     */
    public static enum ShootingResult {NO_ARROWS, MISSED, WUMPUS_KILLED, DEAD};
    
    /**
     * This is the game map.
     */
    private GameMap gameMap;
    
    /**
     * The current player position.
     */
    private Point playerPosition;
    
    /**
     * Contains the discovered cells by the player.<br>
     * If a cell in [x][y] is true, the cell has been discovered.
     */
    private boolean[][] discoveredCells;
    
    /**
     * Indicates if the player is alive.
     */
    private boolean isPlayerAlive;
    
    /**
     * The number of movements done by the player on the game.
     */
    private int movements;
    
    /**
     * Indicates if the player has the gold (true) or not (false).
     */
    private boolean playerHasGold;
    
    /**
     * Indicates if the Wumpus is alive (true) or not (false).
     */
    private boolean wumpusIsAlive;
    
    /**
     * Indicates the current number of arrows the player has.
     */
    private int arrows;
    
    /**
     * Constructor of the class.
     * @param width The width of the game map.
     * @param height The height of the game map.
     * @param arrows The starting number of arrows.
     */
    public Game(final int width, final int height, int arrows) {
        gameMap = new GameMap(width, height);
        discoveredCells = new boolean[gameMap.getWidth()][gameMap.getHeight()];
        this.arrows = arrows;
        playerPosition = new Point(GameMap.STARTING_POSITION);
        discoveredCells[playerPosition.x][playerPosition.y] = true;
        isPlayerAlive = true;
        movements = 0;
        playerHasGold = false;
        wumpusIsAlive = true;
    }
    
    /**
     * Getter for the player position.
     * @return The point representing the player position.
     */
    public Point getPlayerPosition() {
        return playerPosition;
    }
    
    /**
     * Getter for the discovered cells.
     * @return The boolean array containing the discovered cells.
     */
    public boolean[][] getDiscoveredCells() { return discoveredCells; }
    
    /**
     * Getter fot the player movements.
     * @return The player movements.
     */
    public int getMovements() { return movements; }
    
    /**
     * Getter for the number of arrows left.
     * @return The number of arrows left.
     */
    public int getArrows() { return arrows; }
    
    /**
     * Calculates the minimum movements needed to solve this map.
     * @return The number of movements needed to solve this map.
     */
    public int getSolutionLength() {
        return gameMap.getSolutionLength();
    }
    
    /**
     * Checks if the given point is inside this map.
     * @param point The point to be checked.
     * @return True if is inside, false if not.
     */
    public boolean isPointIsideMap(final Point point) {
        return gameMap.isPointIsideMap(point);
    }
    
    /**
     * Gets the starting position of the map.
     * @return The starting position of the map.
     */
    public Point getStartingPosition() {
        return GameMap.STARTING_POSITION;
    }
    
    /**
     * Moves the player one cell in the given direction if possible.
     * @param direction The direction to move the player.
     * @return The result of the movement.
     */
    public MoveResult movePlayer(final Direction direction) {
        if(!isPlayerAlive) {
            return new MoveResult(Result.DEAD);
        }
        
        // Check if the player can move on that direction
        Point newPlayerPosition = canMove(direction);
        
        // The player can't move on that direction, hitting the wall
        if(newPlayerPosition == null) {
            return new MoveResult(Result.HIT_WALL);
        }
        
        // The player can move on that direction
        playerPosition = newPlayerPosition;
        movements++;
        discoveredCells[playerPosition.x][playerPosition.y] = true;
        
        // Create the result of moving to that map cell
        MoveResult moveResult = new MoveResult(getCurrentPositionResult());
        switch(moveResult.getResult()) {
            case WIN:
            case DIED_WELL:
            case DIED_WUMPUS:
            case ERROR: {
                return moveResult;
            }
            
            case GOLD: {
                playerHasGold = true;
                break;
            }
        }
        
        // At this point, the player is safe, calculate perceptions of the player
        getPerceptions(moveResult);
        
        return moveResult;
    }
    
    /**
     * Calculates if the player can move in the given direction.
     * @param direction The direction to move the player.
     * @return The new player position or null if the movement is not valid.
     */
    private Point canMove(final Direction direction) {
        // Calculate the offset of the movement
        int dx = 0;
        int dy = 0;
        switch(direction) {
            case UP: {
                dy = -1;
                break;
            }
            
            case DOWN: {
                dy = 1;
                break;
            }
            
            case LEFT: {
                dx = -1;
                break;
            }
            
            case RIGHT: {
                dx = 1;
                break;
            }
        }
        
        // Get the new player position
        Point newPlayerPosition = new Point(playerPosition);
        newPlayerPosition.offset(dx, dy);
        
        // Check if the new position is inside the map
        if(newPlayerPosition.x < 0 || newPlayerPosition.y < 0) {
            return null;
        }
        
        if(newPlayerPosition.x >= gameMap.getWidth() || newPlayerPosition.y >= gameMap.getHeight()) {
            return null;
        }
        
        return newPlayerPosition;
    }
    
    /**
     * Determines the result of being in the map cell where the player is.
     * @return The result to be calculated.
     */
    private Result getCurrentPositionResult() {
        // Get the cell where the player moved
        MapCell mapCell = gameMap.getMapCells()[playerPosition.x][playerPosition.y];
        
        // Check the cell type to determine the result of the current position of the player
        switch(mapCell.getType()) {
            case SOLUTION:
            case EMPTY: {
                return Result.SAFE;
            }
            
            case WELL: {
                return Result.DIED_WELL;
            }
            
            case WUMPUS: {
                // If the Wumpus is alive, the player dies
                if(wumpusIsAlive) {
                    return Result.DIED_WUMPUS;
                }
                return Result.SAFE;
            }
            
            case GOLD: {
                // The player can get the gold only once
                if(playerHasGold) {
                    return Result.SAFE;
                }
                return Result.GOLD;
            }
            
            case START: {
                // The player wins the game when goes to the starting point with the gold
                if(playerHasGold) {
                    return Result.WIN;
                }
                return Result.SAFE;
            }
            
            default: {
                // This should never happen, but is mandatory for the Java compiler
                return Result.ERROR;
            }
        }
    }
    
    /**
     * Fills the given MoveResult with the perceptions based on the player's position.
     * @param moveResult The MoveResult to be filled with the perceptions.
     */
    private void getPerceptions(final MoveResult moveResult) {
        // Start checking all cells around the player position (3 x 3 square)
        Point currentPoint = new Point(playerPosition);
        currentPoint.offset(-1, -1);
        for(int i = 1; i <= 3; i++) {
            for(int j = 1; j <= 3; j++) {
                // Check that the point is inside the map
                if(gameMap.isPointIsideMap(currentPoint)) {
                    MapCell mapCell = gameMap.getMapCells()[currentPoint.x][currentPoint.y];
                    // Check if in the cell there is a Well, the Wumpus, or the gold
                    switch (mapCell.getType()) {
                        case WELL: {
                            moveResult.perceivesWell();
                            break;
                        }
        
                        case WUMPUS: {
                            // The Wumpus can be perceived only if is alive
                            if(wumpusIsAlive) {
                                moveResult.perceivesWumpus();
                            }
                            break;
                        }
        
                        case GOLD: {
                            // The player can perceive the gold only if doesn't have it
                            if(!playerHasGold) {
                                moveResult.perceivesGold();
                            }
                            break;
                        }
                    }
                }
                
                currentPoint.offset(1, 0);
            }
            
            currentPoint.offset(-3, 1);
        }
    }
    
    /**
     * This is the public method to obtain the perceptions. Usually called when starting new game from a safe position.
     * @return The MoveResult (result SAFE) and the perceptions.
     */
    public MoveResult getPerceptions() {
        MoveResult moveResult = new MoveResult(Result.SAFE);
        getPerceptions(moveResult);
        return moveResult;
    }
    
    /**
     * Shoots an arrow (if the player has arrows) in the given direction.
     * @param direction The direction chosen to shoot the arrow.
     * @return The result of the shoot.
     */
    public ShootingResult shootArrow(final Direction direction) {
        if(!isPlayerAlive) {
            return ShootingResult.DEAD;
        }
        
        // No arrows left
        if(arrows <= 0) {
            return ShootingResult.NO_ARROWS;
        }
        
        arrows--;
        ShootingResult shootingResult = null;
        
        switch (direction) {
            case UP: {
                shootingResult = shootArrowUp();
                break;
            }
            
            case DOWN: {
                shootingResult = shootArrowDown();
                break;
            }
            
            case LEFT: {
                shootingResult = shootArrowLeft();
                break;
            }
            
            case RIGHT: {
                shootingResult = shootArrowRight();
                break;
            }
        }
        
        // If the wumpus is still alive, kill it
        if(shootingResult == ShootingResult.WUMPUS_KILLED) {
            wumpusIsAlive = false;
        }
        
        return shootingResult;
    }
    
    /**
     * Calculates the result of shooting an arrow below the player position.
     * @return The result of the shoot.
     */
    private ShootingResult shootArrowUp() {
        // Start from player position
        Point currentPoint = new Point(playerPosition);
        
        // Move the point to the next cell to be checked
        currentPoint.offset(0, -1);
        
        // Check all cells that are above the player
        while(currentPoint.y >= 0) {
            MapCell mapCell = gameMap.getMapCells()[currentPoint.x][currentPoint.y];
            // If the cell contains the Wumpus, and still alive it gets killed
            if(mapCell.getType() == MapCell.CellType.WUMPUS && wumpusIsAlive) {
                return ShootingResult.WUMPUS_KILLED;
            }
            
            currentPoint.offset(0, -1);
        }
        
        return ShootingResult.MISSED;
    }
    
    /**
     * Calculates the result of shooting an arrow under the player position.
     * @return The result of the shoot.
     */
    private ShootingResult shootArrowDown() {
        // Start from player position
        Point currentPoint = new Point(playerPosition);
    
        // Move the point to the next cell to be checked
        currentPoint.offset(0, 1);
    
        // Check all cells that are below the player
        while(currentPoint.y < gameMap.getHeight()) {
            MapCell mapCell = gameMap.getMapCells()[currentPoint.x][currentPoint.y];
            // If the cell contains the Wumpus, and still alive it gets killed
            if(mapCell.getType() == MapCell.CellType.WUMPUS && wumpusIsAlive) {
                return ShootingResult.WUMPUS_KILLED;
            }
    
            currentPoint.offset(0, 1);
        }
    
        return ShootingResult.MISSED;
    }
    
    /**
     * Calculates the result of shooting an arrow from the player position to the left.
     * @return The result of the shoot.
     */
    private ShootingResult shootArrowLeft() {
        // Start from player position
        Point currentPoint = new Point(playerPosition);
        
        // Move the point to the next cell to be checked
        currentPoint.offset(-1, 0);
        
        // Check all cells that are to the left of the player
        while(currentPoint.x >= 0) {
            MapCell mapCell = gameMap.getMapCells()[currentPoint.x][currentPoint.y];
            // If the cell contains the Wumpus, and still alive it gets killed
            if(mapCell.getType() == MapCell.CellType.WUMPUS && wumpusIsAlive) {
                return ShootingResult.WUMPUS_KILLED;
            }
            
            currentPoint.offset(-1, 0);
        }
    
        return ShootingResult.MISSED;
    }
    
    /**
     * Calculates the result of shooting an arrow from the player position to the right.
     * @return The result of the shoot.
     */
    private ShootingResult shootArrowRight() {
        // Start from player position
        Point currentPoint = new Point(playerPosition);
    
        // Move the point to the next cell to be checked
        currentPoint.offset(1, 0);
    
        // Check all cells that are to the right of the player
        while(currentPoint.x < gameMap.getWidth()) {
            MapCell mapCell = gameMap.getMapCells()[currentPoint.x][currentPoint.y];
            // If the cell contains the Wumpus, and still alive it gets killed
            if(mapCell.getType() == MapCell.CellType.WUMPUS && wumpusIsAlive) {
                return ShootingResult.WUMPUS_KILLED;
            }
    
            currentPoint.offset(1, 0);
        }
    
        return ShootingResult.MISSED;
    }
}
