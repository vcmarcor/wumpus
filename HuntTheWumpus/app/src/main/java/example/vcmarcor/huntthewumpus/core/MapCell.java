package example.vcmarcor.huntthewumpus.core;

/**
 * Created by victor on 28/10/17.<br>
 *
 * This object represents a cell from the game map.
 */
public class MapCell {

    /**
     * Cell type, it can be:<br>
     *     -START (this is an empty cell where the player starts)<br>
     *     -EMPTY (nothing inside)<br>
     *     -GOLD (the gold is inside)<br>
     *     -WELL (a well is inside)<br>
     *     -WUMPUS (the Wumpus is inside)<br>
     *     -SOLUTION (this is a special cell, is empty, but is one of the cells that conform the solution path)
     */
    public enum CellType {START, EMPTY, GOLD, WELL, WUMPUS, SOLUTION};

    /**
     * This is the cell type for this object. It will be one of the cell types of {@link CellType}
     */
    private CellType cellType;
    
    /**
     * Constructor of the class.
     */
    public MapCell() {
        this.cellType = CellType.EMPTY;
    }

    /**
     * Default constructor.
     * @param cellType The cell type to assign.
     */
    public MapCell(final CellType cellType) {
        this.cellType = cellType;
    }

    /**
     * Gets this cell type.
     * @return The cell type.
     */
    public CellType getType() {
        return cellType;
    }
}
