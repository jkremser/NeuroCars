package neurocars.neuralNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Martin Obsahuje data jednoho radku ze vstupniho souboru - tj. 1 vstup
 */
public class DataItem {

	private List<Double> input = null;
	private List<Double> output = null;

	/**
	 * Konstruktor, ktery slouzi k vytvoreni instance, ktera uchovava jen
	 * vstupni data
	 * 
	 * @param inputLength
	 */
	public DataItem(int inputLength) {
		input = new ArrayList<Double>(inputLength);
	}

	/**
	 * Konstruktor, pomoci nehoz muzeme vytvorit instanci uchovavajici: a)
	 * vstupni i vystupni data b) jen vystupni data, pokud inputLength je -1
	 * 
	 * @param inputLength
	 * @param outputLength
	 */
	public DataItem(int inputLength, int outputLength) {
		if (inputLength != -1) {
			input = new ArrayList<Double>(inputLength);
		}
		output = new ArrayList<Double>(outputLength);
	}

	/**
	 * Vrati vstup pro konkretni vstupni neuron
	 * 
	 * @param index
	 *            index vstupniho neuronu
	 * @return
	 */
	public double getInput(int index) {
		return input.get(index);
	}

	/**
	 * Vstupni hodnoty se musi pridavat postupne
	 */
	public void addInputValue(double value) {
		input.add(Double.valueOf(value));
	}

	public void setInput(List<Double> input) {
		this.input = input;
	}

	public void setInput(int index, double value) {
		this.input.set(index, value);
	}

	/**
	 * Vrati pozadovany vstup pro konkretni vystupni neuron
	 * 
	 * @param index
	 *            index vystupniho neuronu
	 * @return
	 */
	public double getOutput(int index) {
		return output.get(index);
	}

	/**
	 * Pozadovane vystupni hodnoty se musi pridavat postupne
	 */
	public void addOutputValue(double value) {
		output.add(Double.valueOf(value));
	}

	public void setOutput(List<Double> output) {
		this.output = output;
	}

	public void setOutput(int index, double value) {
		this.output.set(index, value);
	}

	public String toString() {
		return "IN: " + input + "\nOUT: " + output;
	}
}
