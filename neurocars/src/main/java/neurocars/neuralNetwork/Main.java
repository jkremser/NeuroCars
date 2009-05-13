package neurocars.neuralNetwork;

import java.io.File;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File trainFile = new File("C:\\neurocars\\player1_replay.txt");
		File outputFile = new File("C:\\neurocars\\network");
		double tresholdError = 0.03;
		int hiddenLayersNumber = 1;
		int hiddenLayerSize = 3;
		double learningConstant = 0.1;
		int maxIterations = 1000;
		Network network = new Network(trainFile, tresholdError, outputFile,
				hiddenLayersNumber, hiddenLayerSize, learningConstant,
				maxIterations);
		network.learn();
	}
}
