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
  public static final int UP_DOWN_KEY = 0;
  public static final int LEFT_RIGHT_KEY = 1;
  public static final int SPEED = 2;
  public static final int STEERING_WHEEL = 3;
  public static final int WAYPOINT_DISTANCE = 4;
  public static final int WAYPOINT_ANGLE = 5;
  public static final int CURVE_ANGLE = 6;

  /**
   * Vytvori managera a inicializuje testovaci a trenovaci data
   * 
   * @param trainInput
   *          soubor s trenovacimi daty
   * @param testInput
   *          soubor s testovacimi daty
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
    // Collections.shuffle(trainData);
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
   *          vstupni soubor
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
    int cou = 0;
    try {
      bis.readLine();
      while ((line = bis.readLine()) != null) {
        cou++;
        DataItem item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
        int i = 0;
        String stringValues[] = line.split(";");
        try {
          // WANTED OUTPUT
          item.addOutputValue(Transformer.normalize(Double.parseDouble(stringValues[UP_DOWN_KEY])));
          item.addOutputValue(Transformer.normalize(Double.parseDouble(stringValues[LEFT_RIGHT_KEY])));
          // INPUT
          // double curveAngle = Double.parseDouble(stringValues[CURVE_ANGLE]);
          item.addInputValue(Double.parseDouble(stringValues[SPEED]));
          // item.addInputValue(Double.parseDouble(stringValues[STEERING_WHEEL]));
          item.addInputValue(Double.parseDouble(stringValues[WAYPOINT_DISTANCE]));
          item.addInputValue(Double.parseDouble(stringValues[WAYPOINT_ANGLE]));
          // item.addInputValue(curveAngle);
          // if (curveAngle < 0) {
          // item = Transformer.flip(item);
          // }
        } catch (NumberFormatException nfe) {
          System.err.println("Nepodarilo se provezt konverzi 'String -> Double' na radku "
              + cou);
        }

        // String stringValues[] = line.split(";");
        // try {
        // item.addInputValue(Double.parseDouble(stringValues[4])); //
        // vzdalenost
        // // od dalsiho
        // // WP
        // item.addInputValue((Double.parseDouble(stringValues[6]))); //
        // odchylka
        // // (uhel
        // // zatacky)
        // item.addOutputValue((Double.parseDouble(stringValues[5]) / (2 *
        // Math.PI)) + 0.5);// uhel
        // // do
        // // dalsiho
        // // WP
        // item.addOutputValue(Double.parseDouble(stringValues[2]) / 10.0);//
        // rychlost
        // } catch (NumberFormatException nfe) {
        // System.err.println("Nepodarilo se provezt konverzi 'String -> Double' na radku "
        // + cou);
        // }
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
    return "TRAINDATA:" + trainData + "\nTESTDATA:" + testData;
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
