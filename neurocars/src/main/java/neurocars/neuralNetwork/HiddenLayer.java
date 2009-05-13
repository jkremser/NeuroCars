package neurocars.neuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HiddenLayer implements Serializable {

	private static final long serialVersionUID = 168222070779646673L;
	private int layerIndex; // asi jen k testovcim ucelum
	private List<HiddenNode> nodes;
	private boolean lastHiddenLayer;

	public HiddenLayer(int layerIndex, int size, boolean lastHiddenLayer) {
		this.layerIndex = layerIndex;
		this.lastHiddenLayer = lastHiddenLayer;
		nodes = new ArrayList<HiddenNode>(size);
		for (int i = 0; i < size; i++) {
			nodes.add(new HiddenNode(lastHiddenLayer, size));
		}
	}

	public HiddenNode getNode(int index) {
		return nodes.get(index);
	}

	/**
	 * vsechny hidden neurony z vrstvy vypocitaji svuj opravdovy
	 * (transformovany) vystup a rozeslou ho vsem neuronum dalsi vrstvy
	 */
	public void sendTransformedOutput() {
		for (HiddenNode node : nodes) {
			node.sendTransformedOutput();
		}
	}

}
