package behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import model.Critter;
import model.CritterFactory;
import model.Food;
import model.WorldFactory;
import model.WorldModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {
    @DisplayName("WHEN given a Point p,"
            + "THEN it should be valid if it is within world bounds and is a traversable square")
    @Test
    public void testIsValidPosition() {
        WorldFactory wf = new WorldFactory();
        WorldModel wm = wf.generateTestWorld();
        PathFinder pf = new PathFinder(wm);

        CritterFactory cf = new CritterFactory();
        Critter critter = cf.generateCritter(new Point(2, 2), wm);
        wm.addCritter(critter);

        wm.addFood(new Food(new Point(1, 1), 40, 1));

        assertFalse(pf.isValidPosition(new Point(2, 2)));
        assertTrue(pf.isValidPosition(new Point(1, 1)));
        assertFalse(pf.isValidPosition(new Point(-1, -1)));
        assertFalse(pf.isValidPosition(new Point(1000, 1000)));
    }


    @DisplayName("WHEN given a Point p, "
            + "THEN a list of valid neighbors should be returned")
    @Test
    public void testGetNeighbors() {
        WorldFactory wf = new WorldFactory();
        WorldModel wm = wf.generateTestWorld();
        PathFinder pf = new PathFinder(wm);

        CritterFactory cf = new CritterFactory();
        Critter critter = cf.generateCritter(new Point(2, 2), wm);
        wm.addCritter(critter);

        wm.addFood(new Food(new Point(1, 1), 40, 1));

        assertEquals(3, pf.getNeighbors(new Point(0, 0)).size());
        assertEquals(7, pf.getNeighbors(new Point(1, 2)).size());
    }

    @DisplayName("WHEN given two points, the heuristic function should"
            + "return the Manhattan distance minus the food value")
    @Test
    public void testCalculateHeuristic() {
        WorldFactory wf = new WorldFactory();
        WorldModel wm = wf.generateTestWorld();
        PathFinder pf = new PathFinder(wm);
        Food food = new Food(new Point(10, 10), 40, 1);
        wm.addFood(food);

        assertEquals(10, pf.calculateHeuristic(new Point(0, 0), new Point(10, 0)));
        assertEquals(-20, pf.calculateHeuristic(new Point(0, 0), new Point(10, 10)));
    }
}
