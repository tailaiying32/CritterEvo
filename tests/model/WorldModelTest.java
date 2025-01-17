package model;

import static org.junit.jupiter.api.Assertions.*;

import controller.CritterFactory;
import java.awt.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for WorldModel
 */
public class WorldModelTest {

    @DisplayName("GIVEN a world with width w,"
            + "WHEN the width is set using setWidth to width w1,"
            + "THEN the new width should be w1")
    @Test
    void testSetWidth() {
        WorldModel world = new WorldModel(1, 1, 1, 1, 1, 1, 1);
        world.setWidth(2);
        assertEquals(2, world.getWidth());
    }

    @DisplayName("GIVEN a world with height w,"
            + "WHEN the height is set using setHeight to height h1"
            + "THEN the new height should be h1")
    @Test
    void testSetHeight() {
        WorldModel world = new WorldModel(1, 1, 1, 1, 1, 1, 1);
        world.setHeight(2);
        assertEquals(2, world.getHeight());
    }

    @DisplayName("GIVEN a world with tick count t"
            + "WHEN the tick count is incremented,"
            + "t should increase by 1")
    @Test
    void testIncrementTickCount() {
        WorldModel world = new WorldModel(1, 1, 1, 1, 1, 1, 1);
        assertEquals(1, world.incrementTickCount());
        assertEquals(2, world.incrementTickCount());
    }

    @DisplayName("WHEN a new critter is added to the list of all live critters"
            + "THEN the length of the critter list should increase by 1"
            + "AND the critter should be associated with the point it is on")
    @Test
    void testAddCritter() {
        WorldModel world = new WorldModel(10, 10, 0.0, 0.0, 1, 1, 1);
        world.seedWorld();
        int numCritters = world.getCritters().size();

        CritterFactory critterFactory = new CritterFactory();
        Critter critter = critterFactory.generateCritter(new Point(5, 5), world);
        world.addCritter(critter);

        assertEquals(numCritters + 1, world.getCritters().size());
        assertEquals(world.getCritter(new Point(5, 5)), critter);
    }

    @DisplayName("WHEN a critter at point p is removed from the list of all live critters"
            + "THEN the length of the critter list should decrease by 1"
            + "AND the square at point p should be empty")
    @Test
    void testRemoveCritter() {
        WorldModel world = new WorldModel(10, 10, 0.0, 0.0, 1, 1, 1);
        world.seedWorld();
        CritterFactory critterFactory = new CritterFactory();
        Critter critter = critterFactory.generateCritter(new Point(5, 5), world);
        world.addCritter(critter);

        int numCritters = world.getCritters().size();

        world.removeCritter(critter.getPosition());
        assertEquals(numCritters - 1, world.getCritters().size());
        assertNull(world.getCritter(new Point(5, 5)));
    }

    @DisplayName("WHEN a new food is added at point p,"
            + "THEN the length of the foods list should increase by 1"
            + "and there should be a food at point p")
    @Test
    void testAddFood() {
        WorldModel world = new WorldModel(10, 10, 0.0, 0.0, 1, 1, 1);
        world.seedWorld();
        int numFoods = world.getFoods().size();

        Food food = new Food(new Point(5, 5), 40, 0);
        world.addFood(food);

        assertEquals(numFoods + 1, world.getFoods().size());
        assertEquals(world.getFood(new Point(5, 5)), food);
    }

    @DisplayName("WHEN a food is removed from point p,"
            + "THEN the length of the foods list should decrease by 1"
            + "AND there should be no food at point p")
    @Test
    void testRemoveFood() {
        WorldModel world = new WorldModel(10, 10, 0.0, 0.0, 1, 1, 1);
        world.seedWorld();

        Food food = new Food(new Point(5, 5), 40, 0);
        world.addFood(food);
        int numFoods = world.getFoods().size();

        world.removeFood(new Point(5, 5));
        assertEquals(numFoods - 1, world.getFoods().size());
        assertNull(world.getFood(new Point(5, 5)));
    }
}
