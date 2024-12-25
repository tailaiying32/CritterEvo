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
     * Constructs the world view. Takes in a world model as a parameter, and sets the dimensions of the panel
     * equal to the height and width multiplied by cell size.
     */
    public WorldView(WorldModel worldModel, int cellSize) {
        this.worldModel = worldModel;
        this.cellSize = cellSize;

        setPreferredSize(new Dimension(
                worldModel.getWidth() * cellSize,
                worldModel.getHeight() * cellSize
        ));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // get the 2D array representing the world
        int[][] world = worldModel.getWorld();
        int rows = worldModel.getHeight();
        int cols = worldModel.getWidth();

        // Draw the grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int cellState = world[row][col];

                // Set color based on the state of the cell
                switch (cellState) {
                    case 0 -> g.setColor(Color.WHITE); // Grass
                    case 1 -> g.setColor(Color.BLACK); // Mountain
                    case 2 -> g.setColor(Color.GREEN); // Food
                    case 3 -> g.setColor(Color.BLUE); // Water
                }

                // Draw cell
                int x = col * cellSize;
                int y = row * cellSize;
                g.fillRect(x, y, cellSize, cellSize);

                // Draw grid lines
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }

        // Draw the critters on top of map
        worldModel.getCritters().forEach((id, critter) -> {
           int x = (int) critter.getPosition().getX() * cellSize;
           int y = (int) critter.getPosition().getY() * cellSize;
           g.setColor(Color.ORANGE);
           g.fillOval(x, y, cellSize, cellSize); // Show critter as an orange circle
        });
    }
}
