package behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import model.Critter;
import model.Critter.Orientation;
import model.Critter.Priority;
import controller.CritterFactory;
import model.Food;
import model.Water;
import model.WorldFactory;
import model.WorldModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CritterAITest {

    @DisplayName("WHEN the critter has a priority,"
            + "THEN it should locate the nearest instance of that resource")
    @Test
    public void testLocateTarget() {
        CritterFactory factory = new CritterFactory();
        WorldFactory worldFactory = new WorldFactory();
        WorldModel world = worldFactory.generateTestWorld();

        Critter critter = factory.generateCritter(new Point(10, 10), world);
        world.addCritter(critter);
        critter.setPriority(Priority.FOOD);

        Food food1 = new Food(new Point (0, 0), 10, 0);
        world.addFood(food1);
        Food food2 = new Food(new Point (5, 5), 10, 0);
        world.addFood(food2);
        Food food3 = new Food(new Point (11, 11), 10, 0);
        world.addFood(food3);

        Point nearestTargetFood = critter.locateTarget();
        assertEquals(new Point(11, 11), nearestTargetFood);

        critter.setPriority(Priority.WATER);
        Water water1 = new Water(new Point(0, 1), 0);
        world.addWater(water1);
        Water water2 = new Water(new Point(10, 11), 0);
        world.addWater(water2);
        Point nearestTargetWater = critter.locateTarget();
        assertEquals(new Point(10, 11), nearestTargetWater);
    }

    @DisplayName("WHEN a critter's priority is set,"
            + "THEN it should determine its orientation based on its current position")
    @Test
    public void testDetermineOrientation() {
        CritterFactory factory = new CritterFactory();
        WorldFactory worldFactory = new WorldFactory();
        WorldModel world = worldFactory.generateTestWorld();

        Critter critter = factory.generateCritter(new Point(10, 10), world);
        critter.setPriority(Priority.FOOD);

        Food food1 = new Food(new Point (0, 0), 10, 0);
        world.addFood(food1);
        Orientation orientation1 = critter.determineOrientation();
        assertEquals(Orientation.NW, orientation1);
        world.removeFood(food1.getPosition());

        Food food2 = new Food(new Point (10, 0), 10, 0);
        world.addFood(food2);
        Orientation orientation2 = critter.determineOrientation();
        assertEquals(Orientation.N, orientation2);
        world.removeFood(food2.getPosition());

        Food food3 = new Food(new Point (20, 0), 10, 0);
        world.addFood(food3);
        Orientation orientation3 = critter.determineOrientation();
        assertEquals(Orientation.NE, orientation3);
        world.removeFood(food3.getPosition());

        Food food4 = new Food(new Point(20, 10), 10, 0);
        world.addFood(food4);
        Orientation orientation4 = critter.determineOrientation();
        assertEquals(Orientation.E, orientation4);
        world.removeFood(food4.getPosition());

        Food food5 = new Food(new Point(20, 20), 10, 0);
        world.addFood(food5);
        Orientation orientation5 = critter.determineOrientation();
        assertEquals(Orientation.SE, orientation5);
        world.removeFood(food5.getPosition());

        Food food6 = new Food(new Point(10, 20), 10, 0);
        world.addFood(food6);
        Orientation orientation6 = critter.determineOrientation();
        assertEquals(Orientation.S, orientation6);
        world.removeFood(food6.getPosition());

        Food food7 = new Food(new Point(0, 20), 10, 0);
        world.addFood(food7);
        Orientation orientation7 = critter.determineOrientation();
        assertEquals(Orientation.SW, orientation7);
        world.removeFood(food7.getPosition());

        Food food8 = new Food(new Point(0, 10), 10, 0);
        world.addFood(food8);
        Orientation orientation8 = critter.determineOrientation();
        assertEquals(Orientation.W, orientation8);
        world.removeFood(food8.getPosition());

        Orientation orientation9 = critter.determineOrientation();
        assertEquals(critter.getOrientation(), orientation9);
    }
}
