package neurocars.neuralNetwork;

import neurocars.neuralNetwork.service.Transformer;

public class OutputNode {
	
	private double weightedInputSum = 0.0;
	private double output;
	private double error;
	
	
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
	
	public double getError(){
		return error;
	}
	
	public void setError(double error){
		this.error = error;
	}

	public void computeOutput() {
		output = Transformer.sigmoidal(weightedInputSum);
	}

}
