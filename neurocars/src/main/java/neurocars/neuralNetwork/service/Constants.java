package neurocars.neuralNetwork.service;

public class Constants {

	private static double learningConstant = 0.1;
	private static double momentum;
	
	
	public static void setLearningConstant(double learningConstant){
		if (learningConstant<=0 || learningConstant>=1){
			throw new IllegalArgumentException("learning constant not in interval (0,1)");
		}
		Constants.learningConstant = learningConstant;
	}
	
	public static double getLearningConstant(){
		return learningConstant;
	}
	
	public static void setMomentum(double momentum){
		if (momentum<=0 || momentum>=1){
			throw new IllegalArgumentException("momentum not in interval (0,1)");
		}
		Constants.momentum = momentum;
	}
	
	public static double getMomentum(){
		return momentum;
	}
}