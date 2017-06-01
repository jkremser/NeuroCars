package neurocars.neuralNetwork;

import java.io.File;

import neurocars.neuralNetwork.service.InputManager;
import neurocars.neuralNetwork.service.InputManagerImpl;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File trainFile = new File("C:\\neurocars\\player1_replay.txt");
		File outputFile = new File("C:\\neurocars\\network");
		double tresholdError = 500;
		int hiddenLayersNumber = 1;
		int hiddenLayerSize = 6;
		double learningConstant = 0.002;
		int maxIterations = 1;
		// vystup
		int iterationStep = 20; // po kolika iteracich se vypise trainError

		InputManager iManager = new InputManagerImpl(trainFile);
		// InputManager iManager = new InputManagerDumbImpl();

		Network network = new Network(tresholdError, outputFile,
				hiddenLayersNumber, hiddenLayerSize, learningConstant,
				maxIterations);
		network.setInputManager(iManager);
		network.setIterationStep(iterationStep);
		network.learn();
		System.out.println(iManager);
	}

}
