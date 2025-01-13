package controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A fixed pool of threads to allow for critters to calculate paths concurrently while improving
 * performance by eliminating thread mechanics
 */
public class PathfindingManager {
    /**
     * The pool of threads
     */
    private final ExecutorService threadPool;

    /**
     * Creates a thread pool with "threadCount" threads
     */
    public PathfindingManager(int threadCount) {
        this.threadPool = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * Submits a new task to the thread pool
     */
    public void submitTask(Runnable task) {
        threadPool.submit(task);
    }

    /**
     * Closes the thread pool for new tasks
     */
    public void shutdown() {
        threadPool.shutdown();
    }
}
