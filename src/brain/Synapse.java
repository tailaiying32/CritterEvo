package brain;

/**
 * A connection between two neurons in the brain. Also known as the connection gene. Holds data
 * on the two end neurons, its weight, and its innovation number
 */
public class Synapse {
    /**
     * The id of the in neuron
     */
    private final Neuron start;

    /**
     * The id of the out neuron
     */
    private final Neuron end;

    /**
     * The weight of this synapse
     */
    private double weight;

    /**
     * boolean representing whether this synapse is enabled
     */
    private boolean enabled;

    /**
     * the innovation number attached to this synapse
     */
    private final int innovation;

    /**
     * Constructs a new synapse with "in" and "out" source and end neurons.
     * The innovation number is incremented from the global innovation number
     */
    public Synapse(Neuron start, Neuron end, double weight, boolean enabled, int innovation) {
        this.start = start;
        this.end = end;
        this.weight = weight;
        this.enabled = enabled;
        this.innovation = innovation;

        start.addOutgoingSynapse(this);
        end.addIncomingSynapse(this);
    }

    /**
     * getter methods for synapse attributes
     */
    public Neuron start() { return start; }
    public Neuron end() { return end; }
    public double weight() { return weight; }
    public boolean isEnabled() { return enabled; }
    public int innovation() { return innovation; }

    /**
     * setter methods for weight and enabled
     */
    public void setWeight(double weight) { this.weight = weight; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
