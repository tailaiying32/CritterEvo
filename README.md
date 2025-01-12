# CritterEvo

CritterEvo is a Java-based artificial life simulator, inspired by evolutionary biology and game theory, used to model evolutionary dynamics within an artificial ecosystem. Players configure parameters such as world size, resource distribution, and mutation rates to customize a procedurally generated grid-based world populated by autonomous critters whose main goal is to survive and reproduce. Critters interact with their environment and each other, making decisions based on inputs like hunger, thirst, proximity to resources, and population density. Using genetic algorithms and natural selection, these critters adapt over generations, developing unique behavior and trait sets optimized for survival. 

I created this simulation because I was very interested in how I could model the biological process of evolution using concepts I learned in my CS and Networks (game theory) classes. As such, everything in this program: classes, datastructures, algorithms, etc., are all written and implemented from scratch in vanilla Java (and Swing for the GUI).

To run the simulation on your machine, simply download the source code and run the CritterEvoGame class.

## Core Features and Systems

### **Modeling Natural Selection and Evolution**
Each critter holds a set of traits and intelligence that determine its behavior and overall fitness. When the critter reaches a certain set of conditions, it will reproduce to create a child critter. Whenever the critter reproduces, the child has a small chance of mutation, determined by the parent's and base world mutation rate. Each physical trait is independently chanced for mutation, where its value is changed by up to 20%. Over thousands of generations, these mutations allow for dominant sets of traits to emerge, weeding out critters that are not fit, essentially producing a simplified version of natural selection. In my very early preliminary testing, the critters already evolve to occupy certain niches and roles in the ecosystem. By noting the distribution and modality on each individual trait, it seems that a simple food chain develops, where "herbivores", "carnivores", and "omnivores" emerge, with sometimes even more nuanced speciation within each subcategory. It's incredibly interesting to observe, and I'm excited to see where it goes as I make the simulation and the critters that inhabit it increasingly complex. 

### **World Generation**
The world is a simple square-based grid populated by critters, food, mountains, and water. The world itself is procedurally generated to include varied terrain and natural features such as mountain ranges and lakes. To create natural looking terrain, I used Simplex noise (credit to KdotJPG's [OpenSimplex2 noise generator](https://github.com/KdotJPG/OpenSimplex2)), where for each (x, y) coordinate, a noise output is generated. The output is then processed using four octaves, which adds layers of detail ranging from small to large, and normalized to a value in between 0 and 1. This final value is then mapped to a specific terrain feature, such as grass, mountain, or water. At world generation, user-defined initial food and critter density parameters are used to then seed the world with an initial populace of random critters and food.

### **Critters: Basic Behavior and Decision Making** 
Each critter has attributes, such as health, age, hunger, thirst, aggression, offense, defense, vision, etc. that determine its behavior and overall fitness. Additionally, each critter has a "brain" i.e. a neural network to make decisions. Inputs to the network include health, hunger, thirst, amount of water and food nearby, the distance to the nearest food and water, aggression, and population density. The network's output uses this input to then determine the critter's actions, such as moving, rotating, seeking food, seeking water, mating, attacking, etc. 

### **Critters: Brain and AI** 

The simulation employs the NEAT algorithm (NeuroEvolution of Augmenting Topologies), a genetic algorithm for the evolution of artificial neural networks developed by Kenneth Stanley and Risto Miikulainen in 2002 while at the University of Texas Austin. I very much encourage you to read the original research paper to understand the granular details on how the algorithm works (linked [here](https://nn.cs.utexas.edu/downloads/papers/stanley.cec02.pdf)). In short, the algorithm starts with a perceptron-like feed-forward network of only input and output neurons. Each node in the network is a "node gene", and each connection within the network is a "connection gene". Each connection gene that holds data on its two endpoint genes, its weight and bias, whether it is expressed or not, and an "innovation number", used for gene identification during crossovers and mutation. As stated above, when each critter reproduces, its child has a small chance at mutation. This not only applies to the critter's physical traits, but also its intelligence. In the NEAT algorithm, these mutations can change both connection weights and network structures through the addition or removal of neurons and synapses. Similar to how its physical traits determine its fitness, each critter's AI also plays a large role in determining its survival. Over time, unintelligent critters are weeded out, and certain network structure patterns develop, resulting in emergent behavior. This eliminates the need for supervised training, as natural selection dictates what is a "good" network. 

### **Critters: Pathfinding**
In the early stages of development, critters simply located and moved towards their target in a straight line. After I added terrain, this became insufficient, as critters would lock on a target and attempt to cross untraversable terrain such as mountains, get stuck, and die. To solve this problem, I implemented the A* search algorithm for critter pathfinding, where the heuristic function takes in factors such as terrain steepness and target value. This allows the critters to be smarter in their movement and navigate around obstacles efficiently.  
 
