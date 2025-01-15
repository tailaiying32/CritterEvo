package behavior;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Critter;
import model.Critter.Orientation;
import model.Critter.Priority;
import model.CritterFactory;
import model.Food;
import model.Water;
import model.WorldModel;
import model.WorldModel.CellState;

/**
 * class defining the behavior of the critter, and how it interacts with its environment
 * and other critters
 */
public class InteractionManager {
    /**
     * constructor for Interaction Manager
     */
    public InteractionManager() {}

    /**
     * Eat the food directly in front of the critter.
     * Replenish hunger equal to the food's quantity attribute.
     * Returns hunger level after eating.
     * give advantage to larger creatures
     */
    public double eat(Critter critter, Food food) {
//        System.out.println("eating!");
        if (food != null) {
            // add in advantage for larger creatures if more than two creatures arrive at the same food source at the same time
            Point foodPos = food.getPosition();
            WorldModel world = critter.getWorld();

            List<Point> aroundFood = world.squaresAround(foodPos);
            aroundFood.removeIf(p -> world.getCritter(p) == null);
            // ratio to be used to size advantage
            double ratio = 1;

            if (!aroundFood.isEmpty()) {
                List<Double> sizeCrittersAround = new ArrayList<>();
                for (Point p : aroundFood) {
                    sizeCrittersAround.add(world.getCritter(p).getSize());
                }
                // sum up the sizes and calculate the size advantage of this critter
                double sum = 0;
                for (double size : sizeCrittersAround) {
                    sum += size;
                }
                ratio = critter.getSize()/sum;

                // if this critter is the largest, it gets all the food, otherwise it gets nothing
//                double largestSize = 0;
//                for (double size : sizeCrittersAround) {
//                    if (size > largestSize) {
//                        largestSize = size;
//                    }
//                }
//
//                if (critter.getSize() < largestSize) {
//                    ratio = 0;
//                }
            }

//            double sizeBonus = 1.0 + (critter.getSize() / 100.0); // Larger creatures get more from food
            double newHunger = Math.min(
                    (critter.getHunger() + (food.getQuantity()) * ratio),
                    critter.getMaxHunger()
            );
            critter.setHunger(newHunger);

            double newHealth = Math.min(
                    critter.getHealth() + (food.getQuantity() * 0.5),  // Heal based on food quantity
                    critter.getMaxHealth()
            );
            critter.setHealth(newHealth);

            // Remove the food and update the world
            world.removeFood(food.getPosition());
            world.updateWorldArray();

            return critter.getHunger();
        }
        return critter.getHunger();
    }

    /**
     * calculates size advantage when more than one critter arrives at the same food source
     */

    /**
     * Drink the water directly in front of the critter.
     * Fully replenishes thirst.
     * Returns thirst level after drinking.
     */
    public double drink(Critter critter, Water water) {
//        System.out.println("drinking!");
        double newThirst = critter.getMaxThirst();
        critter.setThirst(newThirst);
        return critter.getThirst();
    }

    /**
     * Rotates the critter to a new orientation.
     * Returns new orientation of critter after rotating.
     * Uses up a small amount of hunger proportional to its size.
     */
    public Orientation rotate(Critter critter, Orientation orientation) {
//        System.out.println("rotating!");
        WorldModel world = critter.getWorld();
        // calculate how many unit rotations are needed for the critter to arrive at the new orientation
        int before = critter.getOrientation().getValue();
        int after = orientation.getValue();
        int difference = Math.abs(after - before);

        if (difference > 4) {
            difference = Math.abs(difference - 8);
        }

        critter.setOrientation(orientation);
        double hungerUsed = (world.getBASE_ROTATE_COST() + difference * (world.getROTATE_COST() * Math.pow(critter.getSize(), world.getSIZE_COST())));
        if (before == after) {
            hungerUsed = 0;
        }
        critter.setHunger(Math.max(
                critter.getHunger() - hungerUsed,
                0
        ));

        return critter.getOrientation();
    }

    /**
     * Moves the critter forward in the direction it is facing
     * Uses up hunger proportional to its size
     * Takes in distance parameter: how many units the critter moves
     * Returns the new coordinates after moving (remember that x and y start at 0 and at the top left)
     */
    public Point move(Critter critter, int distance) {
//        System.out.println("moving!");
        WorldModel world = critter.getWorld();
        int x = (int) critter.getPosition().getX();
        int y = (int) critter.getPosition().getY();
        int newX = x;
        int newY = y;

        switch(critter.getOrientation()) {
            case N:  newY = Math.max(y - distance, 0); break;
            case NE: newX = Math.min(x + distance, world.getWidth() - 1);
                newY = Math.max(y - distance, 0); break;
            case E:  newX = Math.min(x + distance, world.getWidth() - 1); break;
            case SE: newX = Math.min(x + distance, world.getWidth() - 1);
                newY = Math.min(y + distance, world.getHeight() - 1); break;
            case S:  newY = Math.min(y + distance, world.getHeight() - 1); break;
            case SW: newX = Math.max(x - distance, 0);
                newY = Math.min(y + distance, world.getHeight() - 1); break;
            case W:  newX = Math.max(x - distance, 0); break;
            case NW: newX = Math.max(x - distance, 0);
                newY = Math.max(y - distance, 0); break;
        }

        // Update position in critters map
        world.removeCritter(critter.getPosition());
        Point newPos = new Point(newX, newY);
        critter.setPosition(newPos);
        world.addCritter(critter);

        // Update hunger
        double hungerUsed =  (world.getBASE_MOVE_COST() + distance * world.getMOVE_COST() * Math.pow(critter.getSize(), world.getSIZE_COST()));
        critter.setHunger(Math.max(critter.getHunger() - hungerUsed, 0));

        // Update world array
        world.updateWorldArray();

        return critter.getPosition();
    }

    /**
     * Creates and returns a new critter based its parent's attributes
     * the critter should have at least one empty square around it to reproduce
     * uses up a large amount of hunger
     */
    public void reproduce(Critter parent) {
        WorldModel world = parent.getWorld();

        double parentMutationRate = parent.getMutationRate();
        double baseMutationRate = parent.getWorld().getMutationRate();
        double combinedMutationRate = parentMutationRate + baseMutationRate;

        // Mutate traits based on the combined mutation rate
        int maxAge = (int) Math.min(Math.round(mutateTrait((double) parent.getMaxAge(), combinedMutationRate)), 2000);
        double maxHunger = Math.min(mutateTrait(parent.getMaxHunger(), combinedMutationRate), 100);
        double maxThirst = Math.min(mutateTrait(parent.getMaxThirst(), combinedMutationRate), 100);
        double size = Math.min(mutateTrait(parent.getSize(), combinedMutationRate), 100);
        double maxHealth = Math.min(mutateTrait(parent.getMaxHealth(), combinedMutationRate), 100);
        double offense = Math.min(mutateTrait(parent.getOffense(), combinedMutationRate), 100);
        double defense = Math.min(mutateTrait(parent.getDefense(), combinedMutationRate), 100);
        double aggression = Math.min(mutateTrait(parent.getAggression(), combinedMutationRate), 100);
        double mutationRate = mutateTrait(parent.getMutationRate(), combinedMutationRate);
        int vision = (int) Math.round(mutateTrait(parent.getVision(), combinedMutationRate));

        if (!emptySquares(parent).isEmpty()) {
            // first determine what square the child should be born on
            Point birthPosition = birthPoint(parent);
            Critter child = new Critter(
                    parent.getAi(),
                    parent.getInteractionManager(),
                    birthPosition,
                    parent.getOrientation(),
                    maxAge,
                    maxHunger,
                    maxThirst,
                    maxHealth,
                    parent.getSex(),
                    size,
                    offense,
                    defense,
                    aggression,
                    mutationRate,
                    vision,
                    parent.getWorld()
            );

            // then add the critter to the world
            parent.getWorld().addCritter(child);
        }
        parent.setHunger(world.getBASE_REPRODUCTION_COST() * parent.getMaxHunger());
    }

    /**
     * Mutates a given trait
     * helper method for reproduce
     */
    private double mutateTrait(double trait, double mutationRate) {
        double random = Math.random();
        if (random < mutationRate) {
            double change = Math.random() / 5;
            return (Math.random() < 0.5) ? trait * (1 - change) : trait * (1 + change);
        }
        return trait;
    }


    /**
     * (critter 1 attacks critter 2).
     * Takes away health from other critter following this equation: B(S1O1/S2D2)^b,
     * where B and b are baseDamage and damageScalingFactor of the world the critter inhabits
     * For now, let B=25, and b=1.3 ---DO NOT HARD CODE THESE NUMBERS INTO THE METHOD.
     * CREATE SOME STATIC VARIABLES INSTEAD
     */
    public void attack(Critter critter1, Critter critter2) {
        System.out.println("attacking!");
        WorldModel world = critter1.getWorld();

        double oldHealth = critter2.getHealth();
        double c1Power = critter1.getSize()*critter1.getOffense();
        double c2Power = critter2.getSize()*critter2.getDefense();
        double attackDamage = Math.pow((world.getBASE_DAMAGE()*(c1Power/c2Power)), world.getDAMAGE_SCALING_FACTOR());
        critter2.setHealth(oldHealth - attackDamage);
    }

    /**
     * The critter dies
     * Turns the current square critter is on into food with quantity proportional to its size / 2
     * Removes itself from list of live critters
     */
    public void die(Critter critter) {
        Point currentPos = critter.getPosition();
        WorldModel world = critter.getWorld();
        critter.getWorld().removeCritter(critter.getPosition());
        Food newFood = new Food(currentPos, (int) (critter.getSize() * 2), 0);
        world.addFood(newFood);
    }

    /**
     * private helper method to calculate where the child should be born
     */
    private Point birthPoint(Critter critter) {
        WorldModel world = critter.getWorld();
        Point position = critter.getPosition();
        Orientation orientation = critter.getOrientation();
        int childX = position.x;
        int childY = position.y;
        Point birthPos = null;

        switch (orientation) {
            case N:  childY = position.y + 1; break;
            case NE: childX = position.x + 1; childY = position.y + 1; break;
            case E:  childX = position.x - 1; break;
            case SE: childX = position.x - 1; childY = position.y - 1; break;
            case S:  childY = position.y - 1; break;
            case SW: childX = position.x + 1; childY = position.y - 1; break;
            case W:  childX = position.x + 1; break;
            case NW: childX = position.x - 1; childY = position.y + 1; break;
        }

        birthPos = new Point(childX, childY);

        // check if the new position is within world bounds or if it is already taken up
        if (childX >= 0 && childX < world.getWidth() &&
                childY >= 0 && childY < world.getHeight() &&
                world.getWorldArray()[childX][childY] == CellState.GRASS) {
            return birthPos;
        }

        // otherwise, choose any random empty square around the parent
        List<Point> emptySquares = emptySquares(critter);
        int randomNum = (int) (Math.random() * emptySquares.size());
        return emptySquares.get(randomNum);
    }

    /**
     * private helper method to return a list of empty squares around the parent
     */
    private List<Point> emptySquares(Critter critter) {
        List<Point> emptySquares = new ArrayList<>();
        Point position = critter.getPosition();
        int currentX = position.x;
        int currentY = position.y;

        emptySquares.add(new Point(currentX, currentY - 1));
        emptySquares.add(new Point(currentX, currentY + 1));
        emptySquares.add(new Point(currentX + 1, currentY));
        emptySquares.add(new Point(currentX - 1, currentY));
        emptySquares.add(new Point(currentX + 1, currentY + 1));
        emptySquares.add(new Point(currentX + 1, currentY - 1));
        emptySquares.add(new Point(currentX - 1, currentY + 1));
        emptySquares.add(new Point(currentX - 1, currentY - 1));

        emptySquares.removeIf(p ->
                p.x < 0 || p.x >= critter.getWorld().getWidth() ||
                        p.y < 0 || p.y >= critter.getWorld().getHeight() ||
                        critter.getWorld().getWorldArray()[p.x][p.y] != CellState.GRASS
        );

        return emptySquares;
    }
}
