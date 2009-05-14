package neurocars.neuralNetwork;

import java.io.File;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File trainFile = new File("C:\\neurocars\\player1_replay-freon.txt");
		File outputFile = new File("C:\\neurocars\\network");
		double tresholdError = 0.5;
		int hiddenLayersNumber = 2;
		int hiddenLayerSize = 10;
		double learningConstant = 0.2;
		int maxIterations = 10000;
		Network network = new Network(trainFile, tresholdError, outputFile,
				hiddenLayersNumber, hiddenLayerSize, learningConstant,
				maxIterations);
		network.learn();
	}
}
