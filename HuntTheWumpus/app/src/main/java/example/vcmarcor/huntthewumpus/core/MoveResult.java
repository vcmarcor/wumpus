package example.vcmarcor.huntthewumpus.core;

/**
 * Created by victor on 29/10/17.<br>
 *
 * This class represents the results of moving the player to a cell of the map.<br>
 * Contains the result of moving, and the perceptions captured by the player.
 */
public class MoveResult {
    
    /**
     * Result of the movement, it can be:<br>
     * -SAFE (The player is safe)<br>
     * -DIED_WELL (The player fell at a well)<br>
     * -DIED_WUMPUS (The player was killed by the Wumpus)<br>
     * -GOLD (The player found the gold)<br>
     * -WIN (The player won the game)<br>
     * -HIT_WALL (The player hit a wall)<br>
     * -DEAD (The player is dead)<br>
     * -ERROR (Something went wrong, this should never happen)<br>
     */
    public enum Result {SAFE, DIED_WELL, DIED_WUMPUS, GOLD, WIN, HIT_WALL, DEAD, ERROR};
    
    /**
     * The result of the move.
     */
    private Result result;
    
    /**
     * Indicates if the player can perceive a well.
     */
    private boolean perceivesWell;
    
    /**
     * Indicates if the player can perceive the Wumpus.
     */
    private boolean perceivesWumpus;
    
    /**
     * Indicates if the player can perceive he gold.
     */
    private boolean perceivesGold;
    
    /**
     * Constructor for the class.
     * @param result The result to be set.
     */
    public MoveResult(final Result result) {
        this.result = result;
        perceivesWell = false;
        perceivesWumpus = false;
        perceivesGold = false;
    }
    
    /**
     * Getter for the result.
     * @return The result.
     */
    public Result getResult() {
        return result;
    }
    
    /**
     * The player perceives a well.
     */
    public void perceivesWell() {
        this.perceivesWell = true;
    }
    
    /**
     * Indicates if the player can perceive a well.
     * @return True if can perceive a well, false if not.
     */
    public boolean isPerceivingWell() {
        return perceivesWell;
    }
    
    /**
     * The player perceives the Wumpus.
     */
    public void perceivesWumpus() {
        this.perceivesWumpus = true;
    }
    
    /**
     * Indicates if the player can perceive the Wumpus.
     * @return True if can perceive the Wumpus, false if not.
     */
    public boolean isPerceivingWumpus() {
        return perceivesWumpus;
    }
    
    /**
     * The player perceives the gold.
     */
    public void perceivesGold() {
        this.perceivesGold = true;
    }
    
    /**
     * Indicates if the player can perceive the gold.
     * @return True if can perceive the gold, false if not.
     */
    public boolean isPerceivingGold() {
        return perceivesGold;
    }
}
