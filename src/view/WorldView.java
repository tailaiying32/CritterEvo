package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import model.WorldModel;
import model.WorldModel.CellState;

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
     * Map to hold the sprites in
     */
    private EnumMap<CellState, BufferedImage> sprites;

    /**
     * Constructs the world view. Takes in a world model as a parameter, and sets the dimensions of the panel
     * equal to the height and width multiplied by cell size.
     */
    public WorldView(WorldModel worldModel) {
        this.worldModel = worldModel;

        // Initialize the sprites map
        sprites = new EnumMap<>(CellState.class);
        loadSprites();

        // Set a minimum size to prevent the grid from becoming too small
        setMinimumSize(new Dimension(
                worldModel.getWidth() * MIN_CELL_SIZE,
                worldModel.getHeight() * MIN_CELL_SIZE
        ));
    }

    /**
     * helper method to load sprites
     */
    private void loadSprites() {
        try {
            sprites.put(CellState.GRASS, ImageIO.read(new File("C:\\Users\\taila\\Projects\\CritterEvo\\src\\sprites/Grass.png")));
            sprites.put(CellState.MOUNTAIN, ImageIO.read(new File("C:\\Users\\taila\\Projects\\CritterEvo\\src\\sprites/Mountain.png")));
            sprites.put(CellState.FOOD, ImageIO.read(new File("C:\\Users\\taila\\Projects\\CritterEvo\\src\\sprites/Food.png")));
            sprites.put(CellState.WATER, ImageIO.read(new File("C:\\Users\\taila\\Projects\\CritterEvo\\src\\sprites/Water.png")));
            sprites.put(CellState.PEACEFUL_CRITTER, ImageIO.read(new File("C:\\Users\\taila\\Projects\\CritterEvo\\src\\sprites/Critter.png")));
            sprites.put(CellState.ANGRY_CRITTER, ImageIO.read(new File("C:\\Users\\taila\\Projects\\CritterEvo\\src\\sprites/Critter.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        CellState[][] world = worldModel.getWorldArray();
        int rows = worldModel.getHeight();
        int cols = worldModel.getWidth();

        // Draw the grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                CellState cellState = world[col][row];

//                 Set color based on the state of the cell
                switch (cellState) {
                    case GRASS -> g.setColor(new Color(137, 199, 42)); // Grass
                    case MOUNTAIN -> g.setColor(new Color(103, 73, 35)); // Mountain
                    case FOOD -> g.setColor(new Color(70, 120, 0)); // Food
                    case WATER -> g.setColor(new Color(49, 128, 210));  // Water
                    case PEACEFUL_CRITTER -> g.setColor(new Color(115, 115, 115));
                    case ANGRY_CRITTER -> g.setColor(new Color(147, 0, 0));
                }

                // Draw cell
                int x = xOffset + (col * cellSize);
                int y = yOffset + (row * cellSize);
                g.fillRect(x, y, cellSize, cellSize);

                // Draw grid lines
                g.setColor(new Color(100, 100, 100));
                g.drawRect(x, y, cellSize, cellSize);
            }
        }

//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                CellState cellState = world[col][row];
//                BufferedImage sprite = sprites.get(cellState);
//
//                int x = xOffset + (col * cellSize);
//                int y = yOffset + (row * cellSize);
//
//                if (sprite != null) {
//                    g.drawImage(sprite, x, y, cellSize, cellSize, null);
//                } else {
//                    // Fallback to a color if no image is available
//                    g.setColor(Color.LIGHT_GRAY);
//                    g.fillRect(x, y, cellSize, cellSize);
//                }
//            }
//        }
    }

    /**
     * get the world model
     */
    public WorldModel getWorldModel() {
        return worldModel;
    }
}
