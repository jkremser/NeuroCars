package neurocars.neuralNetwork;

import java.io.File;

import neurocars.neuralNetwork.service.InputManager;
import neurocars.neuralNetwork.service.InputManagerDumbImpl;
import neurocars.neuralNetwork.service.InputManagerImpl;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		File trainFile = new File("C:\\neurocars\\player1_replay-freon.txt");
		File outputFile = new File("C:\\neurocars\\network");
		double tresholdError = 965;
		int hiddenLayersNumber = 2;
		int hiddenLayerSize = 15;
		double learningConstant = 0.002;
		int maxIterations = 1000;
		// vystup
		int iterationStep = 50; // po kolika iteracich se vypise trainError

		InputManager iManager = new InputManagerImpl(trainFile);
		// InputManager iManager = new InputManagerDumbImpl();

		Network network = new Network(tresholdError, outputFile,
				hiddenLayersNumber, hiddenLayerSize, learningConstant,
				maxIterations);
		network.setInputManager(iManager);
		network.setIterationStep(iterationStep);
		network.learn();
	}
}
