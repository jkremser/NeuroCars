package neurocars.neuralNetwork;

import java.util.List;

import neurocars.neuralNetwork.service.Constants;
import neurocars.neuralNetwork.service.WeightInitiator;

public class InputNode {
	
	private double input;
	//vstupni neuron nema chybovou hodnotu
	
	private List<HiddenNode> nextLayerNodes;
	private List<Double> nextLayerNodesWeights;
	
	private WeightInitiator weightInitiator = new WeightInitiator();
	
	public void setInput(double input){
		this.input = input;
	}
	
	/*
	 * @return vystup (tedy i vstup) vazeny vahou daneho neuronu nasledujici vrstvy
	 */
	public double getWeighteOutput(int index){
		return input * nextLayerNodesWeights.get(index);
	}
	
	
	
	/**
	 * Prida hiddenNode a nastavi jeho vahu - nahodna mala hodnota
	 * @param node pridavany node
	 */
	public void addNextLayerNode(HiddenNode node){
		nextLayerNodes.add(node);
		nextLayerNodesWeights.add(weightInitiator.getWeight());
	}
	
	/**
	 * Vrati vahu spoje z tohoto neuronu do neuronu index nasledujici vrstvy
	 * @param index index neuronu, o vahu jehoz spoje se jedna
	 * @return
	 */
	public double getWeight(int index){
		return nextLayerNodesWeights.get(index);
	}
	
	/**
	 * Nastavi vahu spoje z tohoto neuronu do neuronu index nasledujici vrstvy 
	 * @param index index neuronu, jehoz vahu nastavujeme
	 * @param weight hodnota vahy
	 * @return
	 */
	public void setWeight(int index, double weight){
		nextLayerNodesWeights.set(index, Double.valueOf(weight));
	}

	public void sendWeightedOutputs() {
		for (int i=0; i<nextLayerNodes.size(); i++){
		HiddenNode hidNode = nextLayerNodes.get(i);
		hidNode.addWeightedInput(getWeighteOutput(i)); 
		//tady se to zbytecne pocita nekolikrat
		//radsi predpocitat, pak ale vyresit inicializaci pro dalsi vstup!
	   }
	}

	/**
	 * Upravi vahu pro kazdy neuron nasledujici skryte vrstvy 
	 * (podle hodnoty jeho chyby a vstupu do nej)
	 */
	public void adjustWeights() {
		double deltaW;
		double newW;
		for(int i=0; i<nextLayerNodes.size(); i++){
			deltaW = Constants.getLearningConstant() * nextLayerNodes.get(i).getError() * input;
			newW = nextLayerNodesWeights.get(i) + deltaW;
			nextLayerNodesWeights.set(i,newW);
		}
	}
	
	

}
