package neurocars.neuralNetwork;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import neurocars.neuralNetwork.service.Constants;

public class Network {
	
	private static double DEFAULT_LEARNING_CONSTANT = 0.1;
	
	//input data
	private File inputFile;
	private int actualRow; //prave zpracovany radek v souboru
	private int numberOfRows;
	private File outputFile; //tam se serializuje naucena sit
	
	//network
	private int inputSize;
	private int outputSize;
	private int hiddenLayersNumber;
	private int hiddenLayerSize;
	private InputLayer inputLayer;
	private List<HiddenLayer> hiddenLayers;
	private OutputLayer outputLayer;
	
	//other
	private boolean learningMode = true;
	

	
	public Network(File inputDataFile, File outputFile, int inputSize, int outputSize, int hiddenLayersNumber, int hiddenLayerSize,double learningConstant){
		this.inputFile = inputDataFile;
		this.outputFile = outputFile;
		actualRow = -1;
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		this.hiddenLayersNumber = hiddenLayersNumber;
		this.hiddenLayerSize = hiddenLayerSize;
		Constants.setLearningConstant(learningConstant);
	}
	
	public Network(File inputDataFile, File outputFile, int inputSize, int outputSize, int hiddenLayersNumber, int hiddenLayerSize){
		this(inputDataFile,outputFile, inputSize,outputSize,hiddenLayersNumber,hiddenLayerSize, DEFAULT_LEARNING_CONSTANT);
	}
	
	
	
	/**
	 * Otevre vstupni soubor, zjisti pocet radku a ulozi do numberOfRows
	 */
	private void initInputFile(){}
	
	/**
	 * Nastavi ukazatel na novy radek, aby se mohlo prochazet znova
	 */
	private void resetInputFile(){}
	
	private void closeInputFile(){}
	
	/**
	 * @return dalsi DataItem nebo null, pokud je konec souboru
	 */
	private DataItem getNextRow(){
		//actualRow++;
		//learningDataFile je otevreny, nacti radek actualRow
		//preved na DataItem a vrat
		return null;
	}
	
	/**
	 * Vytvori samotnou sit
	 */
	private void initNetwork(){
		inputLayer = new InputLayer(inputSize);
		hiddenLayers = new ArrayList<HiddenLayer>(1);
		HiddenLayer layer;
		HiddenLayer previousLayer = null;
		for (int i =0; i< hiddenLayersNumber; i++ ){
			//vytvorime vrstvu
			layer = new HiddenLayer(i,hiddenLayerSize,(i == (hiddenLayersNumber - 1)));
			hiddenLayers.add(layer);
			
			//vsechny neurony predchozi vrstvy napojime na tyto neurony
			if (i==0){//previous layer is input 
				for(int j = 0; j< inputSize; j++){
					InputNode inputNode = inputLayer.getNode(j);
					for (int k=0; k< hiddenLayerSize; k++){
						inputNode.addNextLayerNode(layer.getNode(k));
					}
				}
			}else{//previous layer is hidden
				for(int j = 0; j< hiddenLayerSize; j++){
					HiddenNode previousLayerNode = previousLayer.getNode(j);
					for (int k=0; k< hiddenLayerSize; k++){
						previousLayerNode.addNextLayerHNode(layer.getNode(k));
					}
				}
			}
			previousLayer = layer;
		}
		
		//vytvorit vystupni vrstvu
		outputLayer = new OutputLayer(outputSize);
		//napojit posledni hidden vrstvu na vystupni
		for(int j = 0; j< hiddenLayerSize; j++){
			HiddenNode previousLayerNode = previousLayer.getNode(j);
			for (int k=0; k< hiddenLayerSize; k++){
				previousLayerNode.addNextLayerONode(outputLayer.getNode(k));
			}
		}
	}
	
	
	/**
	 * Zpracuje vstup, protlaci data pres sit a vysledky jsou v hodnotach output vystupnich neuronu
	 * @param item vstupni data
	 */
	private void processInput(DataItem item){
		//inicializovat weightedInputSum hidden a output neuronu
	    clearWeightedInputSums();
		for(int i=0; i<inputSize; i++){//priradime vstup vstupnim jednotkam
    		InputNode inNode = inputLayer.getNode(i);
    		inNode.setInput(item.getInput(i));
    		inNode.sendWeightedOutputs();
    		//rozesleme transformovane vstupy vzdy do dalsi vrstvy
    		for (int j=0; j< hiddenLayersNumber; j++){
    			hiddenLayers.get(j).sendTransformedOutput();
    		}
    		//vystupni neurony spocitaji svoje vystupy
    		outputLayer.computeOutput();
    	}
	}
	
	
	/**
	 * Spusti uceni
	 */
	public void learn(){
		initInputFile();
		initNetwork();
		DataItem item;
		while(!endConditionFulfilled()){
			resetInputFile();
		    while((item=getNextRow())!= null){
		    	processInput(item);
		    	propagateError(item);
		    	adjustWeights();
		    }
		}
		closeInputFile();
		serializeNetwork();
		learningMode = false;
	}
	
	
	/**
	 * Propagace chyby odspodu. Nejprve vystupni vrstva a pak skryte vrsvy v obracenem poradi
	 * Vstupni vrstva samozrejme chybovou hodnotu nema, ta jen predava vstupy
	 * @param item
	 */
	private void propagateError(DataItem item) {
		for(int i=0; i<outputSize; i++){
			outputLayer.getNode(i).computeError(item.getRequiredOutput(i));
		}
		for(int i = hiddenLayers.size()-1; i >=0; i--) {
			HiddenLayer layer = hiddenLayers.get(i);
			for(int j = 0; j<hiddenLayerSize; j++){
				layer.getNode(j).computeError();
			}
		}
	}
	
	/**
	 * Zmeni se vahy podle jednotlivych chyb.
	 * Vahu si pamatuje vzdy PREDCHOZI vrstva, proto volame jen pro input a hidden
	 */
	public void adjustWeights(){
		for(int i=0; i<inputSize; i++){
			inputLayer.getNode(i).adjustWeights();
		}
		for(int i=0; i< hiddenLayers.size(); i++){
			HiddenLayer layer = hiddenLayers.get(i);
			for(int j = 0; j<hiddenLayerSize; j++){
				layer.getNode(j).adjustWeights();
			}
		}
	}

	/**
	 * Serializuje sit do souboru OutputFile
	 */
	private void serializeNetwork() {
		// TODO Auto-generated method stub
	}

	/**
	 * Vyhodnoti ukoncovaci podminku (napr. chyba pod min. urovni)
	 * @return true pokud je podminka splnena, false jinak
	 */
	public boolean endConditionFulfilled(){
		//asi nejlepe spocitat chybu na trenovacich datech, 
		//pripadne max. pocet iteraci
		//TODO
		return true;
	}
	
	/**
	 * inicializuje inputWeightedSum vsech hidden a output neuronu na 0
	 * je to proto, ze vstup pro sigmoidalni fci se "scita" pomoci addWeightedInput() metod 
	 * a proto je treba ho pred dalsi iteraci vynulovat
	 */
	private void clearWeightedInputSums(){
		for(int i = 0; i< hiddenLayersNumber; i++){
    		HiddenLayer layer = hiddenLayers.get(i); 
    		for(int j=0; j< hiddenLayerSize; j++){
    			layer.getNode(j).initWeightedInputSum();
    		}
    	}
		for(int j=0; j< outputSize; j++){
			outputLayer.getNode(j).initWeightedInputSum();
		}
	}

}
