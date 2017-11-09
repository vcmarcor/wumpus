package example.vcmarcor.huntthewumpus.core;


import android.graphics.Point;

import java.util.Random;
import example.vcmarcor.huntthewumpus.core.MapCell.CellType;

/**
 * Created by victor on 28/10/17.<br>
 *
 * This class represents a game map with width x height cells.
 */
public class GameMap {
    
    /**
     * This is the stating position of the player when a new game starts.
     */
    public static final Point STARTING_POSITION = new Point(0, 0);
    
    /**
     * Minimum map width.
     */
    public static final int MIN_WIDTH = 3;
    
    /**
     * Minimum map height.
     */
    public static final int MIN_HEIGHT = 3;
    
    /**
     * MapActivity width.
     */
    private int width;
    
    /**
     * map height.
     */
    private int height;
    
    /**
     * This array represents the game map and its cells.<br>
     * Each cell is located on a X and Y coordinate starting from 0,0. The cell in coordinate X,Y is
     * located at mapCells[X][Y].
     */
    private MapCell[][] mapCells;
    
    /**
     * This is the point representing where the gold is located
     */
    private Point goldPoint;
    
    /**
     * This is the point representing where the Wumpus is located.
     */
    private Point wumpusPoint;
    
    /**
     * Constructor of the class.
     * @param width The map width.
     * @param height The map height.
     */
    public GameMap(final int width, final int height) {
        this.width = width;
        this.height = height;

        // The map should not be smaller than minimum size (MIN_WIDTH x MIN_HEIGHT)
        if(width < MIN_WIDTH) {
            this.width = MIN_WIDTH;
        }

        if(height < MIN_HEIGHT) {
            this.height = MIN_HEIGHT;
        }
        
        // Initialize the map as empty
        initializeEmptyMap();
        
        // Add the starting point to the map cells
        mapCells[STARTING_POSITION.x][STARTING_POSITION.y] = new MapCell(CellType.START);

        // Generate the map randomly
        generateRandomMap();
    }
    
    /**
     * Getter for the width;
     * @return The map width.
     */
    public int getWidth() { return width; }
    
    /**
     * Getter for the height.
     * @return The map height.
     */
    public int getHeight() { return height; }
    
    /**
     * Getter for the position of the gold.
     * @return The position og the gold.
     */
    public Point getGoldPoint() {
        return goldPoint;
    }
    
    /**
     * Getter for the position of the Wumpus.
     * @return The position of the Wumpus.
     */
    public Point getWunmpusPoint() { return wumpusPoint; }
    
    /**
     * Getter for the map cells array.
     * @return The map cells array.
     */
    public MapCell[][] getMapCells() { return mapCells; }
    
    /**
     * Initializes all map cells as empty cells.
     */
    private void initializeEmptyMap() {
        mapCells = new MapCell[width][height];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                mapCells[i][j] = new MapCell();
            }
        }
    }
    
    /**
     * Generates this map randomly.<br>
     * WARNING! This method is implemented supposing that starting point is on 0,0.<br>
     * If this is changed, the logic must be changed.
     */
    private void generateRandomMap() {
        // Set the gold location
        generateRandomGoldPosition();
        
        // Generate the solution path
        generateSolutionPath();
        
        // Generate the Wumpus location
        generateRandomWumpusPosition();
        
        // Generate the wells on the map
        generateRandomWells();
    }
    
    /**
     * Generates the gold position randomly for this map.
     */
    private void generateRandomGoldPosition() {
        goldPoint = generateRandomEmptyPoint();
        mapCells[goldPoint.x][goldPoint.y] = new MapCell(CellType.GOLD);
    }
    
    /**
     * Generates the solution path fot this map. This is the minimum points the player must go trough to complete the game.
     */
    private void generateSolutionPath() {
        // Calculate the solution length
        int solutionLength = getSolutionLength();
        
        Point currentPoint = new Point(STARTING_POSITION);
        currentPoint.offset(1,0);
        
        // Move in X axis
        while(currentPoint.x < goldPoint.x) {
            mapCells[currentPoint.x][currentPoint.y] = new MapCell(CellType.SOLUTION);
            currentPoint.offset(1, 0);
        }
        
        // This will only happen when the starting position and the gold are at the same position in the Y axis
        if(currentPoint.x != goldPoint.x) {
            currentPoint.offset(-1, 1);
        }
        
        // Move in Y axis
        while(currentPoint.y < goldPoint.y) {
            mapCells[currentPoint.x][currentPoint.y] = new MapCell(CellType.SOLUTION);
            currentPoint.offset(0, 1);
        }
    }
    
    /**
     * Calculates the minimum movements needed to solve this map.
     * @return The number of movements needed to solve this map.
     */
    public int getSolutionLength() {
        return goldPoint.x - STARTING_POSITION.x + goldPoint.y - STARTING_POSITION.y - 1;
    }
    
    /**
     * Generates the Wumpus position randomly for this map.
     */
    private void generateRandomWumpusPosition() {
        wumpusPoint = generateRandomEmptyPoint();
        mapCells[wumpusPoint.x][wumpusPoint.y] = new MapCell(CellType.WUMPUS);
    }
    
    /**
     * Generates a random number of wells and place them over the map.
     */
    private void generateRandomWells() {
        int wellCount = getRandomWellCount();
        for(int i = 1; i < wellCount; i++) {
            Point emptyPoint = generateRandomEmptyPoint();
            mapCells[emptyPoint.x][emptyPoint.y] = new MapCell(CellType.WELL);
        }
    }
    
    /**
     * Generates a random number between 1 and the number of empty cells - 1 which will be the number
     * of wells to place on the map.
     * @return The number of wells.
     */
    private int getRandomWellCount() {
        // Count how many wells can be placed on the map.
        int maxWellCount = 0;
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(mapCells[i][j].getType() == CellType.EMPTY) {
                    maxWellCount++;
                }
            }
        }
        
        // Prevent division by 0
        if (maxWellCount == 0) {
            return 0;
        }
        
        maxWellCount = (maxWellCount / 2) + 1;
        
        // Generate a random number between 1 and (maxWellCount - 1)
        Random random = new Random();
        return random.nextInt(maxWellCount - 1) + 1;
    }
    
    /**
     * Generates a random point which is empty on the map.
     * @return The point generated.
     */
    private Point generateRandomEmptyPoint() {
        Point point = generateRandomPoint();
        while(mapCells[point.x][point.y].getType() != CellType.EMPTY) {
            point = generateRandomPoint();
        }
        
        return point;
    }
    
    /**
     * Generates a random point inside the map bounds.
     * @return The generated point.
     */
    private Point generateRandomPoint() {
        Random random = new Random();
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        return new Point(x, y);
    }
    
    /**
     * Checks if the given point is inside this map.
     * @param point The point to be checked.
     * @return True if is inside, false if not.
     */
    public boolean isPointIsideMap(final Point point) {
        if(point.x < 0 || point.y < 0) {
            return false;
        }
        
        if(point.x >= width || point.y >= height) {
            return false;
        }
        
        return true;
    }
}
