package neurocars.neuralNetwork;

import java.io.File;

import neurocars.neuralNetwork.service.InputManager;
import neurocars.neuralNetwork.service.InputManagerImpl;
import junit.framework.TestCase;

public class NetworkTest extends TestCase {

	// @Test
	public void testLearning() {
		File trainFile = new File("C:\\neurocars\\player1_replay-freon.txt");
		File outputFile = new File("C:\\neurocars\\network");
		double tresholdError = 0.5;
		int hiddenLayersNumber = 2;
		int hiddenLayerSize = 15;
		double learningConstant = 0.2;
		int maxIterations = 1000;
		InputManager iManager = new InputManagerImpl(trainFile);
		// InputManager iManager = new InputManagerDumbImpl();
		assertFalse(iManager.containsTestData());
		Network network = new Network(tresholdError, outputFile,
				hiddenLayersNumber, hiddenLayerSize, learningConstant,
				maxIterations);

		// network.setInputManager(iManager);
		// network.learn();

	}
}
