package neurocars.neuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import neurocars.neuralNetwork.service.Constants;
import neurocars.neuralNetwork.service.Transformer;
import neurocars.neuralNetwork.service.WeightInitiator;

public class HiddenNode implements Serializable {

	/**
	 * TODO:Zvazit, jestli neco nebude transient
	 */
	private static final long serialVersionUID = 9201949309993748246L;
	private double weightedInputSum = 0.0;
	private transient double error;
	private double output;
	private transient WeightInitiator weightInitiator = new WeightInitiator();

	private boolean inLastHiddenLayer;
	// podle inLastHiddenLayer se rozhodnem, zda dalsi neurony budou hidden nebo
	// output
	private List<HiddenNode> nextLayerHNodes;
	private List<Double> nextLayerHNodesWeights;

	private List<OutputNode> nextLayerONodes;
	private List<Double> nextLayerONodesWeights;

	public HiddenNode(boolean inLastHiddenLayer, int hiddenLayerSize) {
		this.inLastHiddenLayer = inLastHiddenLayer;
		if (inLastHiddenLayer) {
			nextLayerONodes = new ArrayList<OutputNode>(Network.OUTPUT_SIZE);
			nextLayerONodesWeights = new ArrayList<Double>(Network.OUTPUT_SIZE);
		} else {
			nextLayerHNodes = new ArrayList<HiddenNode>(hiddenLayerSize);
			nextLayerHNodesWeights = new ArrayList<Double>(hiddenLayerSize);
		}
	}

	/**
	 * Prida hiddenNode a nastavi jeho vahu - nahodna mala hodnota
	 * 
	 * @param node
	 *            pridavany node
	 */
	public void addNextLayerHNode(HiddenNode node) {
		nextLayerHNodes.add(node);
		nextLayerHNodesWeights.add(weightInitiator.getWeight());
	}

	/**
	 * Prida outputNode a nastavi jeho vahu - nahodna mala hodnota
	 * 
	 * @param node
	 *            pridavany node
	 */
	public void addNextLayerONode(OutputNode node) {
		nextLayerONodes.add(node);
		nextLayerONodesWeights.add(weightInitiator.getWeight());
	}

	/**
	 * Vrati vahu spoje z tohoto neuronu do neuronu index nasledujici vrstvy
	 * (hidden nebo output podle toho, zda je tento neuron v posledni hidden
	 * vrstve)
	 * 
	 * @param index
	 *            index neuronu, o vahu jehoz spoje se jedna
	 * @return
	 */
	public double getWeight(int index) {
		if (inLastHiddenLayer) {
			return nextLayerONodesWeights.get(index);
		} else {
			return nextLayerHNodesWeights.get(index);
		}

	}

	/**
	 * Nastavi vahu spoje z tohoto neuronu do neuronu index nasledujici vrstvy
	 * (hidden nebo output podle toho, zda je tento neuron v posledni hidden
	 * vrstve)
	 * 
	 * @param index
	 *            index neuronu, jehoz vahu nastavujeme
	 * @param weight
	 *            hodnota vahy
	 * @return
	 */
	public void setWeight(int index, double weight) {
		if (inLastHiddenLayer) {
			nextLayerONodesWeights.set(index, Double.valueOf(weight));
		} else {
			nextLayerHNodesWeights.set(index, Double.valueOf(weight));
		}
	}

	public double getError() {
		return error;
	}

	public void computeError() {
		double sum = 0;
		if (inLastHiddenLayer) {
			for (int i = 0; i < nextLayerONodes.size(); i++) {
				sum += nextLayerONodes.get(i).getError()
						* nextLayerONodesWeights.get(i);
			}
		} else {
			for (int i = 0; i < nextLayerHNodes.size(); i++) {
				sum += nextLayerHNodes.get(i).getError()
						* nextLayerHNodesWeights.get(i);
			}
		}
		error = output * (output - 1) * sum;
	}

	public void initWeightedInputSum() {
		weightedInputSum = 0.0;
	}

	public void addWeightedInput(double weightedInput) {
		this.weightedInputSum += weightedInput;
	}

	public double getOutput() {
		return output;
	}

	// public void setOutput(double output){
	// this.output = output;
	// }

	private void computeOutput() {
		output = Transformer.sigmoidal(weightedInputSum);
	}

	public void sendTransformedOutput() {
		computeOutput();
		if (inLastHiddenLayer) {
			for (int i = 0; i < nextLayerONodes.size(); i++) {
				nextLayerONodes.get(i).addWeightedInput(
						output * nextLayerONodesWeights.get(i));
			}
		} else {
			for (int i = 0; i < nextLayerHNodes.size(); i++) {
				nextLayerHNodes.get(i).addWeightedInput(
						output * nextLayerHNodesWeights.get(i));
			}
		}
	}

	/**
	 * Upravi vahu pro kazdy neuron nasledujici skryte/vystupni vrstvy (podle
	 * hodnoty jeho chyby a vstupu = vystup tohoto neuronu)
	 * 
	 * @param iteration
	 *            iterace, ve ktere se uceni nachazi
	 */
	public void adjustWeights(int iteration) {
		double deltaW;
		double newW;
		if (inLastHiddenLayer) {
			for (int i = 0; i < nextLayerONodes.size(); i++) {
				deltaW = Constants.getLearningConstant(iteration)
						* nextLayerONodes.get(i).getError() * output;
				newW = nextLayerONodesWeights.get(i) + deltaW;
				nextLayerONodesWeights.set(i, newW);
			}
		} else {
			for (int i = 0; i < nextLayerHNodes.size(); i++) {
				deltaW = Constants.getLearningConstant(iteration)
						* nextLayerHNodes.get(i).getError() * output;
				newW = nextLayerHNodesWeights.get(i) + deltaW;
				nextLayerHNodesWeights.set(i, newW);
			}

		}
	}

	public String toString() {
		StringBuilder value = new StringBuilder();
		value.append("]\nweights:\n[");
		if (inLastHiddenLayer) {
			for (Double weight : nextLayerONodesWeights) {
				value.append(weight);
				value.append(",");
			}
		} else {
			for (Double weight : nextLayerONodesWeights) {
				value.append(weight);
				value.append(",");
			}
		}
		value.append("]\n");
		value.append("output:" + output + '\n');
		return value.toString();
	}

}
