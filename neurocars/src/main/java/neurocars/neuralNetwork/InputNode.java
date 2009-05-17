package neurocars.neuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import neurocars.neuralNetwork.service.Constants;
import neurocars.neuralNetwork.service.WeightInitiator;

public class InputNode implements Serializable {

	private static final long serialVersionUID = 7523348218377983154L;

	private double input;
	// vstupni neuron nema chybovou hodnotu
	private List<HiddenNode> nextLayerNodes;
	private List<Double> nextLayerNodesWeights;
	private transient WeightInitiator weightInitiator = new WeightInitiator();

	public InputNode(int hiddenLayerSize) {
		nextLayerNodes = new ArrayList<HiddenNode>(hiddenLayerSize);
		nextLayerNodesWeights = new ArrayList<Double>(hiddenLayerSize);
	}

	public void setInput(double input) {
		this.input = input;
	}

	/*
	 * @return vystup (tedy i vstup) vazeny vahou daneho neuronu nasledujici
	 * vrstvy
	 */
	public double getWeighteOutput(int index) {
		return input * nextLayerNodesWeights.get(index);
	}

	/**
	 * Prida hiddenNode a nastavi jeho vahu - nahodna mala hodnota
	 * 
	 * @param node
	 *            pridavany node
	 */
	public void addNextLayerNode(HiddenNode node) {
		if (nextLayerNodes == null)
			System.out.println("null");

		nextLayerNodes.add(node);
		nextLayerNodesWeights.add(weightInitiator.getWeight());
	}

	/**
	 * Vrati vahu spoje z tohoto neuronu do neuronu index nasledujici vrstvy
	 * 
	 * @param index
	 *            index neuronu, o vahu jehoz spoje se jedna
	 * @return
	 */
	public double getWeight(int index) {
		return nextLayerNodesWeights.get(index);
	}

	/**
	 * Nastavi vahu spoje z tohoto neuronu do neuronu index nasledujici vrstvy
	 * 
	 * @param index
	 *            index neuronu, jehoz vahu nastavujeme
	 * @param weight
	 *            hodnota vahy
	 * @return
	 */
	public void setWeight(int index, double weight) {
		nextLayerNodesWeights.set(index, Double.valueOf(weight));
	}

	public void sendWeightedOutputs() {
		for (int i = 0; i < nextLayerNodes.size(); i++) {
			HiddenNode hidNode = nextLayerNodes.get(i);
			hidNode.addWeightedInput(getWeighteOutput(i));
		}
	}

	/**
	 * Upravi vahu pro kazdy neuron nasledujici skryte vrstvy (podle hodnoty
	 * jeho chyby a vstupu do nej)
	 * 
	 * @param iteration
	 *            iterace, ve ktere se uceni nachazi
	 */
	public void adjustWeights(int iteration) {
		double deltaW;
		double newW;
		for (int i = 0; i < nextLayerNodes.size(); i++) {
			deltaW = Constants.getLearningConstant(iteration)
					* nextLayerNodes.get(i).getError() * input;
			newW = nextLayerNodesWeights.get(i) + deltaW;
			nextLayerNodesWeights.set(i, newW);
		}
	}

	public String toString() {
		StringBuilder value = new StringBuilder();
		// value.append("nodes:\n[");
		// for (HiddenNode node : nextLayerNodes) {
		// value.append(node);
		// }
		value.append("]\nweights:\n[");
		for (Double weight : nextLayerNodesWeights) {
			value.append(weight);
			value.append(",");
		}
		value.append("]\n");
		return value.toString();
	}
}
