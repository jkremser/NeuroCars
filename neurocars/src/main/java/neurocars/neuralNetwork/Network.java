package neurocars.neuralNetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import neurocars.neuralNetwork.service.Constants;
import neurocars.neuralNetwork.service.InputManager;
import neurocars.neuralNetwork.service.Transformer;
import neurocars.utils.AppUtils;
import neurocars.valueobj.NeuralNetworkInput;
import neurocars.valueobj.NeuralNetworkOutput;

public class Network implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8306840890842813850L;
  private static final double DOUBLE_PRECISION = 0.05D;

  // data
  private File outputFile; // tam se serializuje naucena sit
  private transient InputManager inputManager;

  // network
  public static final int INPUT_SIZE = 2;
  public static final int OUTPUT_SIZE = 2;
  public transient static final int DEFAULT_HIDDEN_LAYERS_NUMBER = 1;
  private int hiddenLayersNumber;
  private int hiddenLayerSize;
  private InputLayer inputLayer;
  private List<HiddenLayer> hiddenLayers;
  private OutputLayer outputLayer;

  // learning
  public transient static final double DEFAULT_LEARNING_CONSTANT = 0.1;
  private transient EndConditionType endCondition;
  private transient double trainError;
  private transient double tresholdError;
  private transient int maxIterations;
  private transient int iteration;

  // ladeni
  private int iterationStep = 20;

  // stav site
  private volatile boolean learningMode;

  /**
   * Konstruktor site. Pred samotnym trenovanim je jeste treba zavolat metodu
   * setInputManager() Ten take podle dat, ktere obsahuje urci, zda se bude
   * pocitat chyba z testovacich, nebo trenovacich dat
   * 
   * @param tresholdError
   *          pokud je skutecna chyba mensi, nez tresholdError, uceni se ukonci
   * @param outputFile
   *          soubor urceny na serializaci naucene site
   * @param hiddenLayersNumber
   *          pocet skrytych vrstev
   * @param hiddenLayerSize
   *          pocet neuronu ve skrytych vrstvach
   * @param learningConstant
   *          ucici konstanta
   * @param maxIterations
   *          maximalni pocet ucicich iteraci. Je to vedlejsi podminka, aby se
   *          uceni vzdy zastavilo, i kdyby chyba neklesala.
   */
  public Network(double tresholdError, File outputFile, int hiddenLayersNumber,
      int hiddenLayerSize, double learningConstant, int maxIterations) {
    this.tresholdError = tresholdError;
    this.outputFile = outputFile;
    this.hiddenLayersNumber = hiddenLayersNumber;
    this.hiddenLayerSize = hiddenLayerSize;
    Constants.setLearningConstant(learningConstant);
    this.maxIterations = maxIterations;
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
    System.out.println("loadnig network from file");
    System.out.println("learnigMode: " + net.learningMode);
    return net;
  }

  /**
   * Vytvori samotnou sit
   */
  private void initNetwork() {
    learningMode = true;
    inputLayer = new InputLayer(INPUT_SIZE, hiddenLayerSize);
    hiddenLayers = new ArrayList<HiddenLayer>(1);
    HiddenLayer layer;
    HiddenLayer previousLayer = null;
    for (int i = 0; i < hiddenLayersNumber; i++) {
      // vytvorime hidden vrstvy
      layer = new HiddenLayer(i, hiddenLayerSize,
          (i == (hiddenLayersNumber - 1)));
      hiddenLayers.add(layer);

      // System.out.println("Hidden layer #" + i);
      // vsechny neurony predchozi vrstvy napojime na tyto neurony
      if (i == 0) {// previous layer is input
        for (int j = 0; j < INPUT_SIZE; j++) {
          InputNode inputNode = inputLayer.getNode(j);
          for (int k = 0; k < hiddenLayerSize; k++) {
            // if (j == 2) {
            // inputNode.addNextLayerNode(layer.getNode(k), 2.0); // priorita na
            // vzdalenost
            // } else if (j == 3) {
            // inputNode.addNextLayerNode(layer.getNode(k), 1.0); // priorita na
            // uhel zatacky
            // } else {
            inputNode.addNextLayerNode(layer.getNode(k));
            // }
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
    // System.out.println("OutputLayer.size():" + outputLayer.size());
    // System.out.println("OUTPUT_SIZE:" + OUTPUT_SIZE);
    // System.out.println("lastLayerIndex:" +
    // previousLayer.getLayerIndex());
    // System.out.println("hiddenLayerSize:" + hiddenLayerSize);
    for (int j = 0; j < hiddenLayerSize; j++) {
      HiddenNode previousLayerNode = previousLayer.getNode(j);
      for (int k = 0; k < OUTPUT_SIZE; k++) {
        // System.out.println("k:" + k);
        previousLayerNode.addNextLayerONode(outputLayer.getNode(k));
      }
    }
  }

  /**
   * Zpracuje vstup, protlaci data pres sit a vysledky jsou v hodnotach output
   * vystupnich neuronu
   * 
   * @param item
   *          vstupni data
   */
  private void processInput(DataItem item) {
    // inicializovat weightedInputSum hidden a output neuronu
    clearWeightedInputSums();
    for (int i = 0; i < INPUT_SIZE; i++) {// priradime vstup vstupnim
      // jednotkam
      InputNode inNode = inputLayer.getNode(i);
      inNode.setInput(item.getInput(i));
      inNode.sendWeightedOutputs();
      // // rozesleme transformovane vstupy vzdy do dalsi vrstvy
      // for (int j = 0; j < hiddenLayersNumber; j++) {
      // hiddenLayers.get(j).sendTransformedOutput();
      // }
      // // vystupni neurony spocitaji svoje vystupy
      // outputLayer.computeOutput();
    }
    // rozesleme transformovane vstupy vzdy do dalsi vrstvy
    for (int j = 0; j < hiddenLayersNumber; j++) {
      hiddenLayers.get(j).sendTransformedOutput();
    }
    // vystupni neurony spocitaji svoje vystupy
    outputLayer.computeOutput();
  }

  /**
   * Spusti uceni
   */
  public void learn() {
    new Thread() {

      public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            System.in));
        try {
          String command = reader.readLine();
          if ("stop".equals(command)) {
            learningMode = false;
          }
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }.start();

    // System.out.println("endcondition" + endCondition);
    System.out.println("iteration:" + iteration);
    System.out.println("trainError: " + trainError);
    if (inputManager == null) {
      throw new IllegalStateException("InputManager not set yet");
    }
    inputManager.initTrainData();
    initNetwork();
    // System.out.println(this);
    DataItem item;
    iteration = 0;
    // System.out.println("endcondition:" + endConditionFulfilled());
    while ((!endConditionFulfilled() || iteration == 0) && learningMode) {
      // pred prvni iteraci je totiz trainError 0 < tresholdError a proto
      // i endConditionFulfilled() je true
      if (endCondition == EndConditionType.TRAIN_ERROR) {
        trainError = 0;
      }
      inputManager.resetTrainData();
      while ((item = inputManager.getNextTrainItem()) != null) {
        processInput(item);
        if (endCondition == EndConditionType.TRAIN_ERROR) {
          updateTrainError(item);
        }
        propagateError(item);
        adjustWeights(iteration);
      }
      iteration++;
      if (iteration % iterationStep == 0 || iteration == 1) {
        System.out.println("iteration:" + iteration);
        System.out.println("trainError: " + trainError);
        System.out.println(Constants.getLearningConstant(iteration));
      }
      // if (iteration == 1 || iteration == 1000) {
      // System.out.println(this);
      // }
    }
    System.out.println(this);
    System.out.println(testNet());
    // inputManager.closeTrainData();
    learningMode = false;
    serializeNetwork();
    System.out.println("iteration:" + iteration);
    System.out.println(outputLayer);
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
   * 
   * @param iteration
   *          iterace, ve ktere se uceni nachazi
   */
  private void adjustWeights(int iteration) {
    for (int i = 0; i < INPUT_SIZE; i++) {
      inputLayer.getNode(i).adjustWeights(iteration);
    }
    for (int i = 0; i < hiddenLayers.size(); i++) {
      HiddenLayer layer = hiddenLayers.get(i);
      for (int j = 0; j < hiddenLayerSize; j++) {
        layer.getNode(j).adjustWeights(iteration);
      }
    }
  }

  /**
   * Serializuje sit do souboru OutputFile
   */
  private void serializeNetwork() {
    ObjectOutputStream oos = null;
    try {
      System.out.println("serialize");
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
  private boolean endConditionFulfilled() {
    // System.out.println("maxIterations:" + maxIterations);
    boolean condition = (iteration == maxIterations);
    // System.out.println("condition:" + condition);
    if (endCondition == EndConditionType.TRAIN_ERROR) {
      // System.out.println("trainError" + trainError);
      condition |= (trainError <= tresholdError);
    }
    if (endCondition == EndConditionType.TEST_ERROR) {
      condition |= (computeTotalTestError() <= tresholdError);
    }
    return condition;
  }

  /**
   * Spocita celkova chyba na testovacich datech. Metodu neni vhodne pouzivat,
   * pokud chcemem chybu urcit rovnou z trenovacich dat. V takovem pripade by se
   * data prochazela dvakrat. Vhodnejsi by bylo spocitat chybu kazde iterace a
   * pricist do promenne.
   * 
   * @return celkova chyba
   */
  public double computeTotalTestError() {
    inputManager.initTestData();
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
    // System.out.println("trainError: " + trainError);
  }

  /**
   * Vypocita chybu pro 1 datovou polozku, tj. suma pres d (td - od)
   * 
   * @param item
   * @return chyba na dane polozce
   */
  private double computeErrorForOneItem(DataItem item) {
    double error = 0;
    // System.out.println("DataItem: \n" + item);
    for (int i = 0; i < OUTPUT_SIZE; i++) {
      OutputNode node = outputLayer.getNode(i);
      // System.out.println("real output: " + node.getOutput());
      // System.out.println("expected output: " + item.getOutput(i));
      double aux = item.getOutput(i) - node.getOutput();
      error += aux * aux;
      // operace!
      // System.out.println("Single node error:"
      // + Math.pow(item.getOutput(i) - node.getOutput(), 2));
    }
    // System.out.println("Single item error:" + error);
    // System.out.println("------------------------------------");
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
   * Preda vstup jiz naucene siti (ziskane metodou loadNetwork) a obdrzi vystup.
   * Jedine tato metoda se pouziva k online ovladani auta siti.
   * 
   * @param input
   *          vstup
   * @return vystup
   */
  public NeuralNetworkOutput runNetwork(NeuralNetworkInput input) {
    if (learningMode) {
      throw new java.lang.IllegalStateException(
          "This method cannot be called on network, that is not learned yet");
    }
    DataItem item = Transformer.nnInputToDataItem(input);
    processInput(item);
    System.out.println("\n\n\n" + item);
    DataItem outputDI = getOutput();
    System.out.println(outputDI);
    return Transformer.dataItemToNnOutput(outputDI);
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

  /**
   * Vrati vystup vystupnich neuronu
   * 
   * @return instance DataItem obsahujici jen vystupni data
   */
  private DataItem getOutputForTesting() {
    DataItem output = new DataItem(-1, OUTPUT_SIZE);
    for (int i = 0; i < OUTPUT_SIZE; i++) {
      double item = outputLayer.getNode(i).getOutput();
      if (item < 0.25) {
        item = 0;
      } else if (item > 0.75) {
        item = 1;
      } else {
        item = 0.5;
      }
      output.addOutputValue(item);
    }
    return output;
  }

  public String testNet() {
    int passed = 0;
    int failed = 0;
    int bothPassed = 0;
    if (inputManager == null) {
      throw new IllegalStateException("InputManager not set yet");
    }
    inputManager.initTrainData();
    DataItem item;
    System.out.println("TESTING..");
    while ((item = inputManager.getNextTrainItem()) != null) {
      System.out.println();
      System.out.println("item: " + item);
      processInput(item);
      DataItem result = getOutputForTesting();
      System.out.println("net real output: " + getOutput()
          + "  net rounded output:" + result);
      boolean cond = true;
      for (int i = 0; i < OUTPUT_SIZE; i++) {
        if (Math.abs(item.getOutput(i) - result.getOutput(i)) < DOUBLE_PRECISION) {
          passed++;
        } else {
          cond = false;
          failed++;
        }
      }
      if (cond) {
        bothPassed++;
      }
    }
    System.out.println("passed: " + passed);
    System.out.println("failed: " + failed);
    double accuracy1 = (double) passed / (passed + failed);
    double accuracy2 = (double) (bothPassed * OUTPUT_SIZE) / (passed + failed);

    return "ACCURACY1: " + AppUtils.getNumberFormat().format(accuracy1 * 100)
        + "%\nACCURACY2: " + AppUtils.getNumberFormat().format(accuracy2 * 100)
        + "%";
  }

  public String toString() {
    String retString = "output file: " + outputFile.getAbsolutePath();
    // retString += "\ninputManager:\n" + inputManager;
    retString += "\nhiddenLayersNumber: " + hiddenLayersNumber;
    retString += "\nhiddenLayerSize: " + hiddenLayerSize;
    retString += "\ninputLayer: " + inputLayer;
    retString += "\nhiddenLayers: " + hiddenLayers;
    retString += "\noutputLayer: " + outputLayer;
    retString += "\nendCondition: " + endCondition;
    retString += "\ntrainError: " + trainError;
    retString += "\ntresholdError: " + tresholdError;
    retString += "\nmaxIterations: " + maxIterations;
    retString += "\niterations: " + iteration;
    retString += "\nlearningMode: " + learningMode;
    return retString;
  }

  // TODO: tyto metody jsou jen testovaci, pozdeji smazat
  public int getHiddenLayerSize() {
    return hiddenLayerSize;
  }

  public void setMaxIterations(int maxIter) {
    this.maxIterations = maxIter;
  }

  /**
   * Tato metoda krome nastaveni instance inputManager take urci, jaky bude typ
   * chyby. Jestli se bude chyba pocitat z trenovacich nebo testovacich dat, je
   * dano obsahem inputManageru
   * 
   * @param inputManager
   *          instance umoznujici pristup k trenovacim (pripadne testovacim)
   *          datum.
   */
  public void setInputManager(InputManager inputManager) {
    if (inputManager.containsTestData()) {
      this.endCondition = EndConditionType.TEST_ERROR;
    } else {
      endCondition = EndConditionType.TRAIN_ERROR;
    }
    this.inputManager = inputManager;
  }

  public boolean isLearningMode() {
    return learningMode;
  }

  public void setIterationStep(int iterationStep) {
    this.iterationStep = iterationStep;
  }

}
