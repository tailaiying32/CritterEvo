package behavior;

import brain.Brain;
import controller.ThreadPool;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Critter;
import model.Critter.Orientation;
import model.Critter.Priority;
import model.Food;
import model.Water;
import model.WorldModel;
import model.WorldModel.CellState;

/**
 * Defines what the critter's next priority is i.e. food, water, or love
 * Defines behavior when critter goes into "fight" state based off game theory principles
 * Defines what the critter does next
 */
public class CritterAI {
    /**
     * The thread pool for this AI
     */
    private ThreadPool threadPool;

    /**
     * Constructor for ai
     */
    public CritterAI(int maxThreads) {
        this.threadPool = new ThreadPool(maxThreads);
    }

    /**
     * Calculates priority based off of critter's current state and attributes
     * Returns the calculated priority
     */
    public void updatePriority(Critter critter) {
        Brain brain = critter.brain();
        double healthInput = critter.getHealth()/critter.getMaxHealth();
        double hungerInput = critter.getHunger()/critter.getMaxHunger();
        double thirstInput = critter.getThirst()/critter.getMaxThirst();
        double populationDensity = Math.pow((critter.getVision() * 2 + 1), 2);

        double[] input = {
                healthInput,
                hungerInput,
                thirstInput
        };

        double[] brainOutput = brain.feedForward(input);
        int actionNeuronIndex = 0;
        for (int i = 0; i < brainOutput.length; i++) {
            if (brainOutput[i] > brainOutput[actionNeuronIndex]) {
                actionNeuronIndex = i;
            }
        }

        if (actionNeuronIndex == 0) {
            critter.setPriority(Priority.FOOD);
        } else if (actionNeuronIndex == 1) {
            critter.setPriority(Priority.WATER);
        } else if (actionNeuronIndex == 2) {
            critter.setPriority(Priority.ATTACK);
        } else if (actionNeuronIndex == 3) {
            critter.setPriority(Priority.LOVE);
        } else {
            critter.setPriority(Priority.REST);
        }

        // If no synapses have been formed yet, just choose a random priority (not love)
        if (critter.getWorld().innovationManager().innovation() == 0) {
            double random = (Math.random() * 4);
            if (random <= 1) {
                critter.setPriority(Priority.FOOD);
            } else if (random <= 2) {
                critter.setPriority(Priority.WATER);
            } else if (random <= 3) {
                critter.setPriority(Priority.ATTACK);
            } else if (random <= 4) {
                critter.setPriority(Priority.REST);
            }
        }

        // If no synapses yet, use basic survival behaviors
        if (critter.getWorld().innovationManager().innovation() == 0) {
            if (hungerInput < 0.3 || thirstInput < 0.3) {
                // Prioritize most urgent need
                critter.setPriority(hungerInput < thirstInput ? Priority.FOOD : Priority.WATER);
            } else if (healthInput < 0.3) {
                // Low health, rest to recover
                critter.setPriority(Priority.REST);
            } else {
                // Default exploration behavior
                double random = Math.random();
                if (random <= 0.6) {
                    critter.setPriority(Priority.FOOD); // Bias toward seeking food
                } else if (random <= 0.9) {
                    critter.setPriority(Priority.WATER);
                } else if (random <= 0.95) {
                    critter.setPriority(Priority.REST);
                } else {
                    critter.setPriority(Priority.ATTACK);
                }
            }
        }

        // Controlled reproduction - require good health and resources
        boolean canReproduce = critter.getHealth() >= 0.7 * critter.getMaxHealth() &&
                critter.getHunger() >= 0.7 * critter.getMaxHunger() &&
                critter.getThirst() >= 0.7 * critter.getMaxThirst();

        if (canReproduce && Math.random() <= 0.05) {
            critter.setPriority(Priority.LOVE);
        }
    }

    /**
     * Has the critter take turn based on its priority and state
     */
    public void makeMove(Critter critter) {
        critter.updatePriority();
        WorldModel world = critter.getWorld();
        Pathfinder pathfinder = critter.getPathfinder();

        updateCritter(critter); // update the critters state for each turn

        // reproduce if priority is love
        if (critter.getPriority() == Priority.LOVE) {
            critter.reproduce();
        }

        // locate target and path to target
        Runnable pathfindingTask = () -> {
            synchronized (critter) {
                Point target = locateTarget(critter, critter.getPriority());
                List<Point> path = pathfinder.findPath(critter.getPosition(), target);
                critter.setCurrentPath(path);
            }
        };
        threadPool.submitTask(pathfindingTask);

        // Determine the orientation we need to face the target and rotate if critter is facing the wrong way
        Orientation properOrientation = determineOrientation(critter);
        if (!critter.getOrientation().equals(properOrientation)) {
            critter.rotate(properOrientation);
        }

        // then take action
        takeAction(critter);
    }

    /**
     * Once all move conditions have been completed, the critter takes an action
     */
    private void takeAction(Critter critter) {
        // Check if there's food or water in front of us
        WorldModel world = critter.getWorld();
        Point frontSquare = squareInFront(critter);
        if (frontSquare != null) {
            Food foodInFront = world.getFood(frontSquare);
            if (foodInFront != null) {
                critter.eat(foodInFront);
                return;
            }

            Water waterInFront = world.getWater(frontSquare);
            if (waterInFront != null && critter.getPriority() == Priority.WATER) {
                critter.drink(waterInFront);
                return;
            }

            Critter critterInFront = world.getCritter(frontSquare);
            if (critterInFront != null && critter.getPriority() == Priority.ATTACK) {
                critter.attack(critterInFront);
                return;
            }

            // If there's nothing to eat/drink in front, and we can move, then move
            if (isValidMove(frontSquare, world)) {
                critter.move(1);
            }
        }
    }

    /**
     * Check if a move to the given position is valid
     */
    private boolean isValidMove(Point pos, WorldModel world) {
        // Check if position is within world bounds
        if (pos.x < 0 || pos.x >= world.getWidth() ||
                pos.y < 0 || pos.y >= world.getHeight()) {
            return false;
        }

        // Check if position is occupied by a mountain (1) or another critter (4) or water
        CellState[][] worldArray = world.getWorldArray();
        return worldArray[pos.x][pos.y] != CellState.MOUNTAIN &&
                worldArray[pos.x][pos.y] != CellState.WATER &&
                worldArray[pos.x][pos.y] != CellState.PEACEFUL_CRITTER &&
                worldArray[pos.x][pos.y] != CellState.ANGRY_CRITTER;
    }

    /**
     * locates the nearest instance of the critter's target
     * !!! Eventually change to A* algorithm, but I will figure that out later
     */
    public Point locateTarget(Critter critter, Priority priority) {
        WorldModel world = critter.getWorld();
        Point currentPos = critter.getPosition();

        double shortestDistance = Double.MAX_VALUE;
        Point nearestTarget = currentPos;

        // Get the appropriate target set based on priority
        Map<Point, ?> targets = switch (priority) {
            case FOOD -> world.getFoods();
            case WATER -> world.getWaters();
            case ATTACK -> world.getCritters();
            default -> new HashMap<>();
        };

        // Use Euclidean distance for initial target selection
        for (Point target : targets.keySet()) {
            double distance = Math.sqrt(Math.pow((target.x - currentPos.x), 2) + Math.pow((target.y - currentPos.y), 2));
            if (distance < shortestDistance && distance < critter.getVision()) {
                shortestDistance = distance;
                nearestTarget = target;
            }
        }

        return nearestTarget;
    }

    /**
     * calculates the direction (orientation) in which the critter needs to move
     */
    public Orientation determineOrientation(Critter critter) {
        // the square that the critter needs to move onto next
        if (critter.getCurrentPath().size() < 2) {
            return critter.getOrientation(); // Fallback to the current orientation
        }
        Point nextPoint = critter.getCurrentPath().get(1);

        int dx = nextPoint.x - critter.getPosition().x;
        int dy = nextPoint.y - critter.getPosition().y;

        if (dx > 0 && dy > 0) {
            return Orientation.SE;
        } else if (dx > 0 && dy < 0) {
            return Orientation.NE;
        } else if (dx < 0 && dy > 0) {
            return Orientation.SW;
        } else if (dx < 0 && dy < 0) {
            return Orientation.NW;
        } else if (dx == 0 && dy < 0) {
            return Orientation.N;
        } else if (dx == 0 && dy > 0) {
            return Orientation.S;
        } else if (dx > 0 && dy == 0) {
            return Orientation.E;
        } else if (dx < 0 && dy == 0 ) {
            return Orientation.W;
        } else {
            return critter.getOrientation();
        }
    }

    /**
     * private helper method for calculating the Manhattan distance between two points
     */
    private int calculateDistance(Point p1, Point p2) {
        return Math.abs((p1.x - p2.x)) + Math.abs((p1.y - p2.y));
    }

    /**
     * private helper method for determining the coordinates of the square in front of the critter
     */
    private Point squareInFront(Critter critter) {
        Orientation orientation = critter.getOrientation();
        Point position = critter.getPosition();
        Point squareInFront = null;
        WorldModel world = critter.getWorld();

        int newX = position.x;
        int newY = position.y;

        switch (orientation) {
            case N:  newY = position.y - 1; break;
            case NE: newX = position.x + 1; newY = position.y - 1; break;
            case E:  newX = position.x + 1; break;
            case SE: newX = position.x + 1; newY = position.y + 1; break;
            case S:  newY = position.y + 1; break;
            case SW: newX = position.x - 1; newY = position.y + 1; break;
            case W:  newX = position.x - 1; break;
            case NW: newX = position.x - 1; newY = position.y - 1; break;
        }

        // Check if the new position is within world bounds
        if (newX >= 0 && newX < world.getWidth() &&
                newY >= 0 && newY < world.getHeight()) {
            return new Point(newX, newY);
        }

        return null;
    }

    /**
     * Updates critter per turn before making any moves
     */
    private void updateCritter(Critter critter) {
        reduceHunger(critter);
        reduceThirst(critter);
        heal(critter);
        age(critter);
    }

    /**
     * private helper method for reducing base hunger per turn
     */
    private void reduceHunger(Critter critter) {
        WorldModel world = critter.getWorld();

        // Calculate size factor: larger critters expend more energy
        double sizeFactor = Math.sqrt(Math.log((critter.getSize()) + 2)); // Adding 2 to avoid log(0)

        // Base hunger expenditure (scaled by size)
        double baseExpenditure = world.getBASE_HUNGER_EXPENDITURE() * sizeFactor;

        // Apply rest reduction: e.g., 50% less expenditure while resting
        if (critter.getPriority() == Priority.REST) {
            baseExpenditure *= 0.5;
        }

        // Update hunger, ensuring it doesn't go below 0
        critter.setHunger(Math.max(0, critter.getHunger() - baseExpenditure));
    }

    /**
     * private helper method for reducing base thirst per turn
     */
    private void reduceThirst(Critter critter) {
        WorldModel world = critter.getWorld();
        // take away some thirst, even when resting
        critter.setThirst(critter.getThirst() - (world.getBASE_THIRST_EXPENDITURE()));
    }

    /**
     * private helper method for healing when hunger and thirst are high enough
     */
    private void heal(Critter critter) {
        if (critter.getHunger() > critter.getMaxHunger() * 0.8) {
            double newHealth = Math.min(
                    critter.getHealth() + critter.getMaxHealth() * 0.01,  // Regenerate 1% health per tick
                    critter.getMaxHealth()    // But don't exceed maxHealth
            );
            critter.setHealth(newHealth);
        }
    }

    /**
     * private helper method for aging the critter per turn
     */
    private void age(Critter critter) {
        // increment age by 1
        critter.setAge(critter.getAge() + 1);
    }
}

