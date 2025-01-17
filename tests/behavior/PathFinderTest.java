package behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.util.List;
import model.Critter;
import controller.CritterFactory;
import model.Food;
import model.WorldFactory;
import model.WorldModel;
import model.WorldModel.CellState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {
    @DisplayName("WHEN given a Point p,"
            + "THEN it should be valid if it is within world bounds and is a traversable square")
        @Test
        public void testIsValidPosition() {
            WorldFactory wf = new WorldFactory();
            WorldModel wm = wf.generateTestWorld();
            Pathfinder pf = new Pathfinder(wm);

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
        Pathfinder pf = new Pathfinder(wm);

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
        Pathfinder pf = new Pathfinder(wm);
        Food food = new Food(new Point(10, 10), 40, 1);
        wm.addFood(food);

        assertEquals(10, pf.calculateHeuristic(new Point(0, 0), new Point(10, 0)));
        assertEquals(-20, pf.calculateHeuristic(new Point(0, 0), new Point(10, 10)));
    }

    @DisplayName("GIVEN a start point and an end point"
            + "THEN return a list of PathNodes representing the shortest path in between them")
    @Test
    public void testFindPath() {
        WorldFactory wf = new WorldFactory();
        WorldModel wm = wf.generateTestWorld();
        Pathfinder pf = new Pathfinder(wm);
        CritterFactory cf = new CritterFactory();

        List<Point> path1 = pf.findPath(new Point(0, 0), new Point(5, 5));
        assertEquals(5, path1.size());

        wm.getWorldArray()[1][1] = CellState.MOUNTAIN;

        Critter c1 = cf.generateCritter(new Point(1, 1), wm);
        wm.addCritter(c1);
        List<Point> path2 = pf.findPath(new Point(0, 0), new Point(5, 5));
        assertEquals(6, path2.size());

        wm.addFood(new Food(new Point(2, 1), 40, 0));
        List<Point> path3 = pf.findPath(new Point(0, 0), new Point(5, 5));
        assertEquals(6, path3.size());

        wm.removeFood(new Point(2, 1));
        Critter c2 = cf.generateCritter(new Point(2, 1), wm);
        Critter c3 = cf.generateCritter(new Point(1, 2), wm);
        wm.addCritter(c2);
        wm.addCritter(c3);
        List<Point> path4 = pf.findPath(new Point(0, 0), new Point(5, 5));
        assertEquals(7, path4.size());
    }

    @DisplayName("GIVEN a start and end point"
            + "THEN a direct path between them should be returned")
    @Test
    public void testFindDirectPath() {
        WorldFactory wf = new WorldFactory();
        WorldModel wm = wf.generateTestWorld();
        Pathfinder pf = new Pathfinder(wm);
        CritterFactory cf = new CritterFactory();

        List<Point> path1 = pf.findPath(new Point(0, 0), new Point(4, 4));
        assertEquals(4, path1.size());

        List<Point> path2 = pf.findPath(new Point(0, 0), new Point(0, 4));
    }
}
