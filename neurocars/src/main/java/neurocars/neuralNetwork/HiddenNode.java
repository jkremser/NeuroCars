package neurocars.neuralNetwork;

import java.util.List;

import neurocars.neuralNetwork.service.Transformer;
import neurocars.neuralNetwork.service.WeightInitiator;

public class HiddenNode {

	private double weightedInputSum = 0.0;
	private double error;
	private double output;
	private WeightInitiator weightInitiator = new WeightInitiator(); 
	
	private boolean inLastHiddenLayer;
	//podle inLastHiddenLayer se rozhodnem, zda dalsi neurony budou hidden nebo output
	private List<HiddenNode> nextLayerHNodes;
	private List<Double> nextLayerHNodesWeights;
	
	private List<OutputNode> nextLayerONodes;
	private List<Double> nextLayerONodesWeights;
	
	public HiddenNode(boolean inLastHiddenLayer){
		this.inLastHiddenLayer = inLastHiddenLayer;
	}
	
	/**
	 * Prida hiddenNode a nastavi jeho vahu - nahodna mala hodnota
	 * @param node pridavany node
	 */
	public void addNextLayerHNode(HiddenNode node){
		nextLayerHNodes.add(node);
		nextLayerHNodesWeights.add(weightInitiator.getWeight());
	}
	
	/**
	 * Prida outputNode a nastavi jeho vahu - nahodna mala hodnota
	 * @param node pridavany node
	 */
	public void addNextLayerONode(OutputNode node){
		nextLayerONodes.add(node);
		nextLayerONodesWeights.add(weightInitiator.getWeight());
	}
	
	/**
	 * Vrati vahu spoje z tohoto neuronu do neuronu index nasledujici vrstvy 
	 * (hidden nebo output podle toho, zda je tento neuron v posledni hidden vrstve)
	 * @param index index neuronu, o vahu jehoz spoje se jedna
	 * @return
	 */
	public double getWeight(int index){
		if (inLastHiddenLayer){
			return nextLayerONodesWeights.get(index);	
		}else{
			return nextLayerHNodesWeights.get(index);
		}
		
	}
	
	/**
	 * Nastavi vahu spoje z tohoto neuronu do neuronu index nasledujici vrstvy 
	 * (hidden nebo output podle toho, zda je tento neuron v posledni hidden vrstve)
	 * @param index index neuronu, jehoz vahu nastavujeme
	 * @param weight hodnota vahy
	 * @return
	 */
	public void setWeight(int index, double weight){
		if (inLastHiddenLayer){
			nextLayerONodesWeights.set(index, Double.valueOf(weight));
		}else{
			nextLayerHNodesWeights.set(index, Double.valueOf(weight));
		}
	}
	
	public double getError(){
		return error;
	}
	
	public void setError(double error){
		this.error = error;
	}
	
    public void initWeightedInputSum(){
    	weightedInputSum = 0.0;
    }
    
	public void addWeightedInput(double weightedInput){
		this.weightedInputSum += weightedInput;
	}
	
	public double getOutput(){
		return output;
	}
	
	public void setOutput(double output){
		this.output = output;
	}

	public void sendTransformedOutput() {
		output = Transformer.sigmoidal(weightedInputSum);
		if (inLastHiddenLayer){
			for (int i = 0; i < nextLayerONodes.size(); i++){
				nextLayerONodes.get(i).addWeightedInput(output*nextLayerONodesWeights.get(i));
			}
		}else{
			for (int i = 0; i < nextLayerHNodes.size(); i++){
				nextLayerHNodes.get(i).addWeightedInput(output*nextLayerHNodesWeights.get(i));
			}
		}
		
		
		
	}

	
	
	
}
