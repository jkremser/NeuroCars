package neurocars.neuralNetwork;

import java.util.List;

import neurocars.neuralNetwork.service.Constants;
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
	
	public void computeError(){
		double sum = 0;
		if (inLastHiddenLayer){
			for (int i =0; i<nextLayerONodes.size(); i++){
				sum += nextLayerONodes.get(i).getError() * nextLayerONodesWeights.get(i);
			}
		}else{
			for (int i =0; i<nextLayerHNodes.size(); i++){
				sum += nextLayerHNodes.get(i).getError() * nextLayerHNodesWeights.get(i);
			}
		}
		error = output * (output-1) * sum;
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
	
//	public void setOutput(double output){
//		this.output = output;
//	}

	private void computeOutput() {
		output = Transformer.sigmoidal(weightedInputSum);
	}
	
	public void sendTransformedOutput() {
		computeOutput();
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

	/**
	 * Upravi vahu pro kazdy neuron nasledujici skryte/vystupni vrstvy 
	 * (podle hodnoty jeho chyby a vstupu = vystup tohoto neuronu)
	 */
	public void adjustWeights() {
		double deltaW;
		double newW;
		if(inLastHiddenLayer){
			for(int i=0; i<nextLayerONodes.size(); i++){
				deltaW = Constants.getLearningConstant() * nextLayerONodes.get(i).getError() * output;
				newW = nextLayerONodesWeights.get(i) + deltaW;
				nextLayerONodesWeights.set(i,newW);
			}
		}else{
			for(int i=0; i<nextLayerHNodes.size(); i++){
				deltaW = Constants.getLearningConstant() * nextLayerHNodes.get(i).getError() * output;
				newW = nextLayerHNodesWeights.get(i) + deltaW;
				nextLayerHNodesWeights.set(i,newW);
		    }
		
	    }
	}
	
	
	
}
