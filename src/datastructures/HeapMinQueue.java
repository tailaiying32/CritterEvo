package datastructures;

import datastructures.MinQueue;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A min priority queue of distinct elements of type `KeyType` associated with (extrinsic) integer
 * priorities, implemented using a binary heap paired with a hash table.
 */
public class HeapMinQueue<KeyType> implements MinQueue<KeyType> {

    /**
     * Pairs an element `key` with its associated priority `priority`.
     */
    private record Entry<KeyType>(KeyType key, int priority) {}

    /**
     * Associates each element in the queue with its index in `heap`.  Satisfies
     * `heap.get(index.get(e)).key().equals(e)` if `e` is an element in the queue. Only maps
     * elements that are in the queue (`index.size() == heap.size()`).
     */
    private final Map<KeyType, Integer> index;

    /**
     * Sequence representing a min-heap of element-priority pairs.  Satisfies
     * `heap.get(i).priority() >= heap.get((i-1)/2).priority()` for all `i` in `[1..heap.size()]`.
     */
    private final ArrayList<Entry<KeyType>> heap;

    /**
     * Assert that our class invariant is satisfied.  Returns true if it is (or if assertions are
     * disabled).
     */
    private boolean checkInvariant() {
        for (int i = 1; i < heap.size(); ++i) {
            int p = (i - 1) / 2;
            assert heap.get(i).priority() >= heap.get(p).priority();
            assert index.get(heap.get(i).key()) == i;
        }
        assert index.size() == heap.size();
        return true;
    }

    /**
     * Create an empty queue.
     */
    public HeapMinQueue() {
        index = new HashMap<>();
        heap = new ArrayList<>();
        assert checkInvariant();
    }

    /**
     * Return whether this queue contains no elements.
     */
    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Return the number of elements contained in this queue.
     */
    @Override
    public int size() {
        return heap.size();
    }

    /**
     * Return an element associated with the smallest priority in this queue.  This is the same
     * element that would be removed by a call to `remove()` (assuming no mutations in between).
     * Throws NoSuchElementException if this queue is empty.
     */
    @Override
    public KeyType get() {
        // Propagate exception from `List::getFirst()` if empty.
        return heap.getFirst().key();
    }

    /**
     * Return the minimum priority associated with an element in this queue.  Throws
     * NoSuchElementException if this queue is empty.
     */
    @Override
    public int minPriority() {
        return heap.getFirst().priority();
    }

    /**
     * If `key` is already contained in this queue, change its associated priority to `priority`.
     * Otherwise, add it to this queue with that priority.
     */
    @Override
    public void addOrUpdate(KeyType key, int priority) {
        if (!index.containsKey(key)) {
            add(key, priority);
        } else {
            update(key, priority);
        }
    }

    /**
     * Remove and return the element associated with the smallest priority in this queue.  If
     * multiple elements are tied for the smallest priority, an arbitrary one will be removed.
     * Throws NoSuchElementException if this queue is empty.
     */
    @Override
    public KeyType remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        // get key to return
        KeyType minKey = heap.getFirst().key;

        // remove element from index map
        index.remove(minKey);

        // if this was the last element, just remove it
        if (heap.size() == 1) {
            heap.removeFirst();
            return minKey;
        }

        // replace root with last element and bubble down
        heap.set(0, heap.removeLast());
        bubbleDown(0);

        assert checkInvariant();
        return minKey;
    }

    /**
     * Remove all elements from this queue (making it empty).
     */
    @Override
    public void clear() {
        index.clear();
        heap.clear();
        assert checkInvariant();
    }

    /**
     * Swap the Entries at indices `i` and `j` in `heap`, updating `index` accordingly.  Requires `0
     * <= i,j < heap.size()`.
     */
    private void swap(int i, int j) {
        assert i >= 0 && i < heap.size();
        assert j >= 0 && j < heap.size();

        // get entries at both positions
        Entry<KeyType> entryI = heap.get(i);
        Entry<KeyType> entryJ = heap.get(j);

        // swap entries in heap
        heap.set(i, entryJ);
        heap.set(j, entryI);

        // update index map to reflect new positions
        index.put(entryI.key, j);
        index.put(entryJ.key, i);
    }

    /**
     * Add element `key` to this queue, associated with priority `priority`.  Requires `key` is not
     * contained in this queue.
     */
    private void add(KeyType key, int priority) {
        assert !index.containsKey(key);
        // add new entry to end of heap
        int newIndex = heap.size();
        heap.add(new Entry<>(key, priority));
        index.put(key, newIndex);

        // bubble up to maintain heap property
        bubbleUp(newIndex);

        assert checkInvariant();
    }

    /**
     * Change the priority associated with element `key` to `priority`.  Requires that `key` is
     * contained in this queue.
     */
    private void update(KeyType key, int priority) {
        assert index.containsKey(key);
        // get current index and update priority
        int i = index.get(key);
        int oldPriority = heap.get(i).priority();
        heap.set(i, new Entry<>(key, priority));

        // bubble up or down depending on priority change
        if (priority < oldPriority) {
            bubbleUp(i);
        }
        else if (priority > oldPriority) {
            bubbleDown(i);
        }

        assert checkInvariant();
    }

    /*
     * Moves entry at given index upward in heap until heap property is restored.
     * Returns final index of entry.
     * Requires 0 <= index < heap.size()
     */
    private int bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(parent).priority() <= heap.get(index).priority()) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
        return index;
    }

    /**
     * Moves entry at given index downward in heap until heap property is restored.
     * Returns final index of entry
     * Requires 0 <= index < heap.size()
     */
    private int bubbleDown(int index) {
        int length = heap.size();
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            if (leftChild < length &&
                    heap.get(leftChild).priority() < heap.get(smallest).priority()) {
                smallest = leftChild;
            }
            if (rightChild < length &&
                    heap.get(rightChild).priority() < heap.get(smallest).priority()) {
                smallest = rightChild;
            }

            if (smallest == index) {
                break;
            }

            swap(index, smallest);
            index = smallest;

        }
        return index;
    }
}
