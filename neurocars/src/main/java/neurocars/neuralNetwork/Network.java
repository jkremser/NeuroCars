package neurocars.neuralNetwork;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import neurocars.neuralNetwork.service.Constants;
import neurocars.neuralNetwork.service.InputManager;
import neurocars.neuralNetwork.service.Transformer;
import neurocars.valueobj.NeuralNetworkInput;
import neurocars.valueobj.NeuralNetworkOutput;

public class Network implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 8306840890842813850L;
	// data
	private File outputFile; // tam se serializuje naucena sit
	private transient InputManager inputManager;

	// network
	public static final int INPUT_SIZE = 5;
	public static final int OUTPUT_SIZE = 4;
	private int hiddenLayersNumber;
	private int hiddenLayerSize;
	private InputLayer inputLayer;
	private List<HiddenLayer> hiddenLayers;
	private OutputLayer outputLayer;

	// learning
	private transient static final double DEFAULT_LEARNING_CONSTANT = 0.1;
	private transient EndConditionType endCondition;
	private transient double trainError;
	private transient double tresholdError;
	private transient int maxIterations;
	private transient int iterations;

	// stav site
	private boolean learningMode = true;

	/**
	 * Tento konstruktor pouzijeme, pokud chceme ukoncovaci podminku jako
	 * minimalni chybu na trenovacich datech, tj. testovaci soubor neni treba
	 * specifikovat
	 * 
	 * @param trainFile
	 *            soubor s trenovacimi daty
	 * @param tresholdError
	 *            pokud je skutecna chyba mensi, nez tresholdError, uceni se
	 *            ukonci
	 * @param outputFile
	 *            soubor urceny na serializaci naucene site
	 * @param hiddenLayersNumber
	 *            pocet skrytych vrstev
	 * @param hiddenLayerSize
	 *            pocet neuronu ve skrytych vrstvach
	 * @param learningConstant
	 *            ucici konstanta
	 * @param maxIterations
	 *            maximalni pocet ucicich iteraci. Je to vedlejsi podminka, aby
	 *            se uceni vzdy zastavilo, i kdyby chyba neklesala.
	 */
	public Network(File trainFile, double tresholdError, File outputFile,
			int hiddenLayersNumber, int hiddenLayerSize,
			double learningConstant, int maxIterations) {
		this(trainFile, null, tresholdError, outputFile, hiddenLayersNumber,
				hiddenLayerSize, learningConstant, maxIterations);
	}

	/**
	 * Tento konstruktor pouzijeme, pokud chceme ukoncovaci podminku jako
	 * minimalni chybu na testovacich datech, (anebo na trenovacich, je-li
	 * testFile null)
	 * 
	 * @param trainFile
	 *            soubor s trenovacimi daty
	 * @param testFile
	 *            soubor s testovacimy daty. Pokud je null, nastavi se jina
	 *            ukoncovaci podminka
	 * @param tresholdError
	 *            pokud je skutecna chyba mensi, nez tresholdError, uceni se
	 *            ukonci
	 * @param outputFile
	 *            soubor urceny na serializaci naucene site
	 * @param hiddenLayersNumber
	 *            pocet skrytych vrstev
	 * @param hiddenLayerSize
	 *            pocet neuronu ve skrytych vrstvach
	 * @param learningConstant
	 *            ucici konstanta
	 * @param maxIterations
	 *            maximalni pocet ucicich iteraci. Je to vedlejsi podminka, aby
	 *            se uceni vzdy zastavilo, i kdyby chyba neklesala.
	 */
	public Network(File trainFile, File testFile, double tresholdError,
			File outputFile, int hiddenLayersNumber, int hiddenLayerSize,
			double learningConstant, int maxIterations) {
		if (testFile == null) {
			endCondition = EndConditionType.TRAIN_ERROR;
		} else {
			this.endCondition = EndConditionType.TEST_ERROR;
		}
		inputManager = new InputManager(trainFile, testFile);
		this.tresholdError = tresholdError;
		this.outputFile = outputFile;
		this.hiddenLayersNumber = hiddenLayersNumber;
		this.hiddenLayerSize = hiddenLayerSize;
		Constants.setLearningConstant(learningConstant);
	}

	/**
	 * Nacte neuronovou sit ze souboru
	 * 
	 * @param outputFile
	 */
	public static Network loadNetwork(File outputFile) {
		if (outputFile == null) {
			throw new NullPointerException("outputFile");
		}
		ObjectInputStream ois = null;
		Network net = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(outputFile));
			net = (Network) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return net;
	}

	/**
	 * Vytvori samotnou sit
	 */
	public void initNetwork() {
		inputLayer = new InputLayer(INPUT_SIZE);
		hiddenLayers = new ArrayList<HiddenLayer>(1);
		HiddenLayer layer;
		HiddenLayer previousLayer = null;
		for (int i = 0; i < hiddenLayersNumber; i++) {
			// vytvorime vrstvu
			layer = new HiddenLayer(i, hiddenLayerSize,
					(i == (hiddenLayersNumber - 1)));
			hiddenLayers.add(layer);

			// vsechny neurony predchozi vrstvy napojime na tyto neurony
			if (i == 0) {// previous layer is input
				for (int j = 0; j < INPUT_SIZE; j++) {
					InputNode inputNode = inputLayer.getNode(j);
					for (int k = 0; k < hiddenLayerSize; k++) {
						inputNode.addNextLayerNode(layer.getNode(k));
					}
				}
			} else {// previous layer is hidden
				for (int j = 0; j < hiddenLayerSize; j++) {
					HiddenNode previousLayerNode = previousLayer.getNode(j);
					for (int k = 0; k < hiddenLayerSize; k++) {
						previousLayerNode.addNextLayerHNode(layer.getNode(k));
					}
				}
			}
			previousLayer = layer;
		}

		// vytvorit vystupni vrstvu
		outputLayer = new OutputLayer(OUTPUT_SIZE);
		// napojit posledni hidden vrstvu na vystupni
		for (int j = 0; j < hiddenLayerSize; j++) {
			HiddenNode previousLayerNode = previousLayer.getNode(j);
			for (int k = 0; k < hiddenLayerSize; k++) {
				previousLayerNode.addNextLayerONode(outputLayer.getNode(k));
			}
		}
	}

	/**
	 * Zpracuje vstup, protlaci data pres sit a vysledky jsou v hodnotach output
	 * vystupnich neuronu
	 * 
	 * @param item
	 *            vstupni data
	 */
	private void processInput(DataItem item) {
		// inicializovat weightedInputSum hidden a output neuronu
		clearWeightedInputSums();
		for (int i = 0; i < INPUT_SIZE; i++) {// priradime vstup vstupnim
			// jednotkam
			InputNode inNode = inputLayer.getNode(i);
			inNode.setInput(item.getInput(i));
			inNode.sendWeightedOutputs();
			// rozesleme transformovane vstupy vzdy do dalsi vrstvy
			for (int j = 0; j < hiddenLayersNumber; j++) {
				hiddenLayers.get(j).sendTransformedOutput();
			}
			// vystupni neurony spocitaji svoje vystupy
			outputLayer.computeOutput();
		}
	}

	/**
	 * Spusti uceni
	 */
	public void learn() {
		inputManager.initTrainData();
		initNetwork();
		DataItem item;
		iterations = 0;
		while (!endConditionFulfilled()) {
			if (endCondition == EndConditionType.TRAIN_ERROR) {
				trainError = 0;
			}
			inputManager.resetTrainFile();
			while ((item = inputManager.getNextTrainItem()) != null) {
				processInput(item);
				if (endCondition == EndConditionType.TRAIN_ERROR) {
					updateTrainError(item);
				}
				propagateError(item);
				adjustWeights();
			}
			iterations++;
		}
		inputManager.closeTrainFile();
		serializeNetwork();
		learningMode = false;
	}

	/**
	 * Propagace chyby odspodu. Nejprve vystupni vrstva a pak skryte vrsvy v
	 * obracenem poradi Vstupni vrstva samozrejme chybovou hodnotu nema, ta jen
	 * predava vstupy
	 * 
	 * @param item
	 */
	private void propagateError(DataItem item) {
		for (int i = 0; i < OUTPUT_SIZE; i++) {
			outputLayer.getNode(i).computeError(item.getOutput(i));
		}
		for (int i = hiddenLayers.size() - 1; i >= 0; i--) {
			HiddenLayer layer = hiddenLayers.get(i);
			for (int j = 0; j < hiddenLayerSize; j++) {
				layer.getNode(j).computeError();
			}
		}
	}

	/**
	 * Zmeni se vahy podle jednotlivych chyb. Vahu si pamatuje vzdy PREDCHOZI
	 * vrstva, proto volame jen pro input a hidden
	 */
	public void adjustWeights() {
		for (int i = 0; i < INPUT_SIZE; i++) {
			inputLayer.getNode(i).adjustWeights();
		}
		for (int i = 0; i < hiddenLayers.size(); i++) {
			HiddenLayer layer = hiddenLayers.get(i);
			for (int j = 0; j < hiddenLayerSize; j++) {
				layer.getNode(j).adjustWeights();
			}
		}
	}

	/**
	 * Serializuje sit do souboru OutputFile
	 */
	public void serializeNetwork() {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(outputFile));
			oos.writeObject(this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Vyhodnoti ukoncovaci podminku (napr. chyba pod min. urovni)
	 * 
	 * @return true pokud je podminka splnena, false jinak
	 */
	public boolean endConditionFulfilled() {
		boolean condition = (iterations == maxIterations);
		if (endCondition == EndConditionType.TRAIN_ERROR) {
			condition |= (trainError <= tresholdError);
		}
		if (endCondition == EndConditionType.TEST_ERROR) {
			condition |= (computeTotalTestError() <= tresholdError);
		}
		return condition;
	}

	/**
	 * Spocita celkova chyba na testovacich datech. Metodu neni vhodne pouzivat,
	 * pokud chcemem chybu urcit rovnou z trenovacich dat. V takovem pripade by
	 * se data prochazela dvakrat. Vhodnejsi by bylo spocitat chybu kazde
	 * iterace a pricist do promenne.
	 * 
	 * @return celkova chyba
	 */
	public double computeTotalTestError() {
		inputManager.initTrainData();
		double error = 0;
		DataItem item;
		while ((item = inputManager.getNextTestItem()) != null) {
			processInput(item);
			error += computeErrorForOneItem(item);
		}
		return error;
	}

	/**
	 * Aktualizuje globalni promenou trainError o chybu konkretniho vzoru
	 * 
	 * @param item
	 */
	private void updateTrainError(DataItem item) {
		trainError += computeErrorForOneItem(item);
	}

	/**
	 * Vypocita chybu pro 1 datovou polozku, tj. suma pres d (td - od)
	 * 
	 * @param item
	 * @return chyba na dane polozce
	 */
	private double computeErrorForOneItem(DataItem item) {
		double error = 0;
		for (int i = 0; i < OUTPUT_SIZE; i++) {
			OutputNode node = outputLayer.getNode(i);
			error += Math.pow(item.getOutput(i) - node.getOutput(), 2);
		}
		return error;
	}

	/**
	 * inicializuje inputWeightedSum vsech hidden a output neuronu na 0 je to
	 * proto, ze vstup pro sigmoidalni fci se "scita" pomoci addWeightedInput()
	 * metod a proto je treba ho pred dalsi iteraci vynulovat
	 */
	private void clearWeightedInputSums() {
		for (int i = 0; i < hiddenLayersNumber; i++) {
			HiddenLayer layer = hiddenLayers.get(i);
			for (int j = 0; j < hiddenLayerSize; j++) {
				layer.getNode(j).initWeightedInputSum();
			}
		}
		for (int j = 0; j < OUTPUT_SIZE; j++) {
			outputLayer.getNode(j).initWeightedInputSum();
		}
	}

	/**
	 * Preda vstup jiz naucene siti (ziskane metodou loadNetwork) a obdrzi
	 * vystup. Jedine tato metoda se pouziva k online ovladani auta siti.
	 * 
	 * @param input
	 *            vstup
	 * @return vystup
	 */
	public NeuralNetworkOutput runNetwork(NeuralNetworkInput input) {
		if (learningMode) {
			throw new java.lang.IllegalStateException(
					"This method cannot be called on network, that is not learned yet");
		}
		DataItem item = Transformer.nnInputToDataItem(input);
		processInput(item);
		DataItem outputDI = getOutput();
		return Transformer.DataItemToNnOutput(outputDI);
	}

	/**
	 * Vrati vystup vystupnich neuronu
	 * 
	 * @return instance DataItem obsahujici jen vystupni data
	 */
	private DataItem getOutput() {
		DataItem output = new DataItem(-1, OUTPUT_SIZE);
		for (int i = 0; i < OUTPUT_SIZE; i++) {
			output.addOutputValue(outputLayer.getNode(i).getOutput());
		}
		return output;
	}

	public String toString() {
		String retString = "NEURAL NETWORK\n\noutput file: "
				+ outputFile.getAbsolutePath();
		retString += "\ninputManager: " + inputManager;
		retString += "\nhiddenLayersNumber: " + hiddenLayersNumber;
		retString += "\nhiddenLayerSize: " + hiddenLayerSize;
		retString += "\ninputLayer: " + inputLayer;
		retString += "\nhiddenLayers: " + hiddenLayers;
		retString += "\noutputLayer: " + outputLayer;
		retString += "\nendCondition: " + endCondition;
		retString += "\ntrainError: " + trainError;
		retString += "\ntresholdError: " + tresholdError;
		retString += "\nmaxIterations: " + maxIterations;
		retString += "\niterations: " + iterations;
		retString += "\nlearningMode: " + learningMode;
		return retString;
	}

	//TODO: tyto metody jsou jen testovaci, pozdeji smazat
	public int getHiddenLayerSize() {
		return getHiddenLayerSize(); 
	}

	public void setMaxIterations(int maxIter) {
		this.maxIterations = maxIter;
	}

	public void setInputManager(InputManager inputManager) {
		this.inputManager = inputManager;
	}
}
