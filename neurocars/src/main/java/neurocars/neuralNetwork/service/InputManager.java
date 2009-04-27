package neurocars.neuralNetwork.service;

import java.io.File;

import neurocars.neuralNetwork.DataItem;

public class InputManager {

	private File trainInput;
	private File testInput;
	
	public InputManager(File trainInput, File testInput){
	  this.trainInput = trainInput;
	  this.testInput = testInput;
	}
	
	/**
	 * Otevre trenovaci soubor
	 */
	public void initTrainFile(){
		//TODO
	}
	
	/**
	 * Nastavi ukazatel na novy radek, aby se mohlo prochazet znova
	 */
	public void resetTrainFile(){
		//TODO
	}
	
	public void closeTrainFile(){
		//TODO
	}
	
	/**
	 * @return dalsi DataItem nebo null, pokud je konec souboru
	 */
	public DataItem getNextTrainItem(){
		//TODO
		//actualRow++;
		//trainInput je otevreny, nacti radek actualRow
		//preved na DataItem a vrat
		return null;
	}
	
	/**
	 * Otevre testovaci soubor
	 */
	public void initTestFile(){
		//TODO
	}
	
	/**
	 * Nastavi ukazatel na novy radek, aby se mohlo prochazet znova
	 */
	public void resetTestFile(){
		//TODO
	}
	
	public void closeTestFile(){
		//TODO
	}
	
	/**
	 * @return dalsi DataItem nebo null, pokud je konec souboru
	 */
	public DataItem getNextTestItem(){
		//TODO
		//actualRow++;
		//trainInput je otevreny, nacti radek actualRow
		//preved na DataItem a vrat
		return null;
	}
	
	/**
	 * Tato metoda by mohla nejak 'nahodne' rozdelit vstup na testovaci a trenovaci,
	 * treba v pomeru 1/9
	 * @param input vstupni soubor	
	 * @param trainFile
	 * @param testFile
	 */
   public static void splitFile(File inputFile, File trainFile, File testFile){
	   //TODO
   }
	
	
	
}
