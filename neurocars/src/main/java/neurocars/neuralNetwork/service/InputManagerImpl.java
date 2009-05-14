package neurocars.neuralNetwork.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import neurocars.neuralNetwork.DataItem;
import neurocars.neuralNetwork.Network;

public class InputManagerImpl implements InputManager {

	public static final int TRAIN_TEST_RATIO = 10;

	private File trainInput;
	private File testInput;
	private List<DataItem> testData = null;
	private List<DataItem> trainData = null;
	private int trainItemCounter = -1;
	private int testItemCounter = -1;

	/**
	 * Vytvori managera a inicializuje testovaci a trenovaci data
	 * 
	 * @param trainInput
	 *            soubor s trenovacimi daty
	 * @param testInput
	 *            soubor s testovacimi daty
	 */
	public InputManagerImpl(File trainInput, File testInput) {
		this.trainInput = trainInput;
		this.testInput = testInput;
	}

	/**
	 * Vytvori managera a inicializuje testovaci a trenovaci data
	 * 
	 * @param trainInput
	 */
	public InputManagerImpl(File trainInput) {
		this.trainInput = trainInput;
		// splitFile(trainInput);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see neurocars.neuralNetwork.service.InputManager#initTrainData()
	 */
	public void initTrainData() {
		if (trainInput != null) {
			trainData = processFile(trainInput);
		}
		resetTrainData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see neurocars.neuralNetwork.service.InputManager#resetTrainData()
	 */
	public void resetTrainData() {
		trainItemCounter = 0;
		Collections.shuffle(trainData);
	}

	/**
	 * zbytecna metoda pri teto implementaci. Ted jsou vsechny data v pameti.
	 * Pokud to zmenime a bude se prubezne prochazet, bufferovat a tak, tak bude
	 * treba soubor zavrit mimo metodu processFile()
	 */
	public void closeTrainData() {
		trainData = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see neurocars.neuralNetwork.service.InputManager#getNextTrainItem()
	 */
	public DataItem getNextTrainItem() {
		return getNextItem(trainItemCounter++, trainData);
	}

	private DataItem getNextItem(int which, List<DataItem> from) {
		if (which < 0) {
			throw new IllegalArgumentException(
					"zaporny index, pravdepodobne je potreba nejprve inicializovat data");
		}
		if (from == null) {
			throw new NullPointerException("from");
		}
		if (from.size() <= which) {
			return null; // empty
		}

		return from.get(which);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see neurocars.neuralNetwork.service.InputManager#initTestData()
	 */
	public void initTestData() {
		if (testInput != null) {
			testData = processFile(testInput);
		}
		resetTestData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see neurocars.neuralNetwork.service.InputManager#resetTestData()
	 */
	public void resetTestData() {
		testItemCounter = 0;
		Collections.shuffle(testData);
	}

	/**
	 * zbytecna metoda
	 */
	public void closeTestData() {
		testData = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see neurocars.neuralNetwork.service.InputManager#getNextTestItem()
	 */
	public DataItem getNextTestItem() {
		return getNextItem(testItemCounter++, testData);
	}

	/**
	 * Tato metoda by mohla nejak 'nahodne' rozdelit vstup na testovaci a
	 * trenovaci, treba v pomeru 1/9
	 * 
	 * @param input
	 *            vstupni soubor
	 * @param trainFile
	 * @param testFile
	 */
	public void splitFile(File inputFile) {
		List<DataItem> all = InputManagerImpl.processFile(inputFile);
		List<DataItem> testData = new ArrayList<DataItem>(128);
		List<DataItem> trainData = new ArrayList<DataItem>(128);
		int capacity = all.size();
		List<Integer> osudi = new ArrayList<Integer>(capacity);
		for (int i = 0; i < capacity; i++) {
			osudi.add(i); // naplni osudi cisly 0 az capacity
		}
		Collections.shuffle(osudi); // zamichej
		int i = 0;
		for (int index : osudi) { // prvni TRAIN_TEST_RATIO-inu dej do
			// testovacich,
			// zbytek do
			// trenovacich
			if (i < capacity / TRAIN_TEST_RATIO) { // testovaci
				testData.add(all.get(index));
			} else { // trenovaci
				trainData.add(all.get(index));
			}
			i++;
		}
		this.testData = testData;
		this.trainData = trainData;
	}

	private static List<DataItem> processFile(File file) {
		if (file == null) {
			throw new NullPointerException("file");
		}
		List<DataItem> data = new ArrayList<DataItem>(128);
		BufferedReader bis = null;
		try {
			bis = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.err.println("Soubor s daty nenalezen.");
			e.printStackTrace();
		}
		String line = null;
		try {
			bis.readLine();
			while ((line = bis.readLine()) != null) {
				DataItem item = new DataItem(Network.INPUT_SIZE,
						Network.OUTPUT_SIZE);
				int i = 0;
				for (String stringValue : line.split(";")) {
					try {
						if (i < Network.OUTPUT_SIZE) {
							item
									.addOutputValue(Double
											.parseDouble(stringValue));
						} else {
							item.addInputValue(Double.parseDouble(stringValue));
						}
					} catch (NumberFormatException nfe) {
						System.err
								.println("Nepodarilo se provezt konverzi 'String -> Double' na retezci "
										+ stringValue);
					}
					i++;
				}
				if (item.getInput(4) < 0) { // uhle zatacky je zaporny
					item.setInput(1, -item.getInput(1)); // prevraceni znamenka
					// u otoceni
					// volantu
					item.setInput(3, -item.getInput(3)); // prevraceni znamenka
					// u uhlu k
					// nasledujicimu bodu
					double aux = item.getOutput(3);
					item.setOutput(3, item.getOutput(2)); // prohodi doleva a
					// doprava
					item.setOutput(2, aux);
				}
				data.add(item);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public String toString() {
		return "TRAINDATA:" + trainData + "\n\nTESTDATA:" + testData;
	}

	/**
	 * Vrati true, pokud instance obsahuje i testovaci data
	 * 
	 * @return true, pokud instance obsahuje i testovaci data
	 */
	public boolean containsTestData() {
		return (testData != null);
	}

}
