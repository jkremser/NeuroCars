package neurocars.neuralNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Martin
 * Obsahuje data jednoho radku ze vstupniho souboru - tj. 1 vstup
 */
public class DataItem {
	
	private List<Double> input ;
	private List<Double> output = null;

	public DataItem(int inputLength){
		input = new ArrayList<Double>(inputLength);
    }
	
	public DataItem(int inputLength, int outputLength){
		input = new ArrayList<Double>(inputLength);
		output = new ArrayList<Double>(outputLength);
	}
	
	
	/**
	 * Vrati vstup pro konkretni vstupni neuron
	 * @param index index vstupniho neuronu
	 * @return
	 */
	public double getInput(int index){
		return input.get(index);
	}
	
	/**
	 * Vstupni hodnoty se musi pridavat postupne
	 */
	public void addInputValue(double value){
		input.add(Double.valueOf(value));
	}
	
	/**
	 * Vrati pozadovany vstup pro konkretni vystupni neuron
	 * @param index index vystupniho neuronu
	 * @return
	 */
	public double getRequiredOutput(int index){
		return input.get(index);
	}
	
	/**
	 * Pozadovane vystupni hodnoty se musi pridavat postupne
	 */
	public void addOutputValue(double value){
		output.add(Double.valueOf(value));
	}
}
