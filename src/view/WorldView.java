package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import model.WorldModel;

/**
 * The GUI representing the world
 */
public class WorldView extends JPanel {

    /**
     * the size of each cell on the grid.
     */
    private int cellSize;

    /**
     * the world the worldview is representing
     */
    private WorldModel worldModel;

    /**
     * minimum cell size to ensure visibility
     */
    private static final int MIN_CELL_SIZE = 4;

    /**
     * Constructs the world view. Takes in a world model as a parameter, and sets the dimensions of the panel
     * equal to the height and width multiplied by cell size.
     */
    public WorldView(WorldModel worldModel) {
        this.worldModel = worldModel;

        // Set a minimum size to prevent the grid from becoming too small
        setMinimumSize(new Dimension(
                worldModel.getWidth() * MIN_CELL_SIZE,
                worldModel.getHeight() * MIN_CELL_SIZE
        ));
    }

    /**
     * Calculate the cell size based on the current panel dimensions
     */
    private int calculateCellSize() {
        int width = getWidth();
        int height = getHeight();

        // Calculate cell size based on available space and grid dimensions
        int cellWidth = width / worldModel.getWidth();
        int cellHeight = height / worldModel.getHeight();

        // Use the smaller of the two to maintain square cells
        return Math.max(MIN_CELL_SIZE, Math.min(cellWidth, cellHeight));
    }


    /**
     * paints the world view
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellSize = calculateCellSize();

        // Calculate offset to center the grid
        int xOffset = (getWidth() - (worldModel.getWidth() * cellSize)) / 2;
        int yOffset = (getHeight() - (worldModel.getHeight() * cellSize)) / 2;

        // get the 2D array representing the world
        int[][] world = worldModel.getWorldArray();
        int rows = worldModel.getHeight();
        int cols = worldModel.getWidth();

        // Draw the grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int cellState = world[col][row];

                // Set color based on the state of the cell
                switch (cellState) {
                    case 0 -> g.setColor(Color.WHITE); // Grass
                    case 1 -> g.setColor(Color.BLACK); // Mountain
                    case 2 -> g.setColor(Color.GREEN); // Food
                    case 3 -> g.setColor(Color.BLUE);  // Water
                    case 4 -> g.setColor(Color.ORANGE); // Critter
                }

                // Draw cell
                int x = xOffset + (col * cellSize);
                int y = yOffset + (row * cellSize);
                g.fillRect(x, y, cellSize, cellSize);

                // Draw grid lines
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }

    /**
     * get the world model
     */
    public WorldModel getWorldModel() {
        return worldModel;
    }
}
