package behavior;

import java.awt.Point;
import model.Critter;
import model.Critter.Priority;

/**
 * Defines what the critter's next priority is i.e. food, water, or love
 * Defines behavior when critter goes into "fight" state based off game theory principles
 * Defines what the critter does next
 */
public class CritterAI {

    /**
     * Constructor for ai
     */
    public CritterAI() {}

    /**
     * Calculates priority based off of critter's current state and attributes
     * Returns the calculated priority
     */
    public Priority updatePriority(Critter critter) {
        if (critter.getHunger() >= 80 && critter.getThirst() >= 80 && critter.getHealth() >= 80) {
            critter.setPriority(Priority.LOVE);
        } else {
            if (critter.getHunger() >= critter.getThirst()) {
                critter.setPriority(Priority.FOOD);
            } else {
                critter.setPriority(Priority.WATER);
            }
        }

        return critter.getPriority();
    }

    /**
     * Has the critter take turn based on its priority and state
     */
    public void makeMove(Critter critter) {
        throw new UnsupportedOperationException();
    }

}
