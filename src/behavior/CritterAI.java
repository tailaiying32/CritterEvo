package behavior;

import model.Critter;
import model.Critter.Priority;
import model.Critter.fightOrFlight;

/**
 * Defines what the critter's next priority is i.e. food, water, or love
 * Defines behavior when critter goes into "fight" state based off game theory principles
 */
public class CritterAI {

    /**
     * Calculates priority based off of critter's current state and attributes
     * Returns the calculated priority
     */
    Priority calculatePriority(Critter critter) {
        throw new UnsupportedOperationException();
    }

    /**
     * Calculates whether critter chooses to fight or run away when there is a conflict
     * of resources based off of its aggression
     * Returns "FIGHT" or "RUN"
     */
    fightOrFlight fightOrFlight(Critter critter) {
        throw new UnsupportedOperationException();
    }
}
