package model;

import java.awt.Point;
import model.Critter.Orientation;
import model.Critter.Sex;

/**
 * used for creating randomized critters at the start of a new world
 */
public class CritterFactory {

    /**
     * constructor for CritterFactory
     */
    public CritterFactory() {}

    /**
     * Creates and returns a critter with randomized attributes
     */
    public Critter generateCritter(Point p) {
        // random number generator for attributes for critter construction
        double or = Math.random() * 4;
        Orientation orientation = null;
        if (or <= 1) {
            orientation = Orientation.UP;
        } else if (or <= 2) {
            orientation = Orientation.DOWN;
        } else if (or <= 3) {
            orientation = Orientation.LEFT;
        } else if (or <= 4) {
            orientation = Orientation.RIGHT;
        }

        int maxAge = (int) (Math.random() * 100);

        int maxHealth = (int) (Math.random() * 100);

        double sx = Math.random() * 2;
        Sex sex = null;
        if (sx <= 1) {
            sex = Sex.MALE;
        } else if (sx <= 2) {
            sex = Sex.FEMALE;
        }

        int size = (int) (Math.random() * 100);

        int offense = (int) (Math.random() * 100);

        int defense = (int) (Math.random() * 100);

        int aggression = (int) (Math.random() * 100);

        int mutationRate = (int) (Math.random() * 10);

        Critter critter = new Critter(
                p,
                orientation,
                maxAge,
                maxHealth,
                sex,
                size,
                offense,
                defense,
                aggression,
                0,
                mutationRate
        );

        return critter;
    }

}
