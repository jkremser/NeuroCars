package neurocars.neuralNetwork.service;

import java.util.Random;

public class WeightInitiator {

	private Random generator = new Random();
	
	/**
	 * 
	 * @return hodnota (-0.05;0.05>
	 */
	public Double getWeight(){
		return Double.valueOf((generator.nextDouble()/10)-0.05);
	}
	
}