package neurocars.neuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InputLayer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7295108673751428247L;
	private List<InputNode> nodes;

	public InputLayer(int size, int hiddenLayerSize) {
		// System.out.println("Creating input layer");
		nodes = new ArrayList<InputNode>(size);
		for (int i = 0; i < size; i++) {
			nodes.add(new InputNode(hiddenLayerSize));
		}
		// System.out.println("inputNodes: " + nodes.size());
	}

	public InputNode getNode(int index) {
		return nodes.get(index);
	}

	public String toString() {
		StringBuilder value = new StringBuilder();
		value.append("nodes:\n[");
		for (InputNode node : nodes) {
			value.append(node);
		}
		value.append("]\n");
		return value.toString();
	}
}
