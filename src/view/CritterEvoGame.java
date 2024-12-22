package view;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * A graphical game to simulate evolution!
 */
public class CritterEvoGame {

    /**
     * The application window
     */
    private final JFrame frame;

    /**
     * Construct a new application instance. Initializes GUI components, so must be invoked on the
     * Swing Dispatch Thread. Does not show application (call start() to do so).
     */
    public CritterEvoGame() {
        // initialize application window
        frame = new JFrame("Critter Evo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // set default area for the frame
        frame.setPreferredSize(new Dimension(1920, 1080));
    }

    // TODO: Add world component (the grid), living on the left majority of frame

    // TODO: Add a control panel on right of frame, with play, pause, reset, and speed sliders

    public void start() {
        frame.pack();
        frame.setVisible(true);
    }

    /** Run an instance of CritterEvoGame. No program arguments are expected.
     */
    public static void main(String[] args) {
        CritterEvoGame game = new CritterEvoGame();
        game.start();
    }
}


