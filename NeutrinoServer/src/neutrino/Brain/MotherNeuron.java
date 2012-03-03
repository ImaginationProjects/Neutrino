package neutrino.Brain;

import java.util.*;

public class MotherNeuron {
	// ANN for Neutrino, so hard D:
	public static List<Neuron> BasicNeuronList;
	public static Map<String, Neuron> NeuronsByActivity;
	public MotherNeuron()
	{
		// here must init and stablish all Neurons
		BasicNeuronList = new ArrayList<Neuron>();
		NeuronsByActivity = new HashMap<String, Neuron>();
		
		
		// must request all neurones to work!
		
	}
}
