package neurocars.neuralNetwork.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import neurocars.neuralNetwork.DataItem;

public class InputManager {

  public static final int INPUT_LENGTH = 6;
  public static final int OUTPUT_LENGTH = 4;
  public static final int TRAIN_TEST_RATIO = 10;

  private File trainInput;
  private File testInput;
  private List<List<String>> testData;
  private List<List<String>> trainData;
  private int trainItemCounter = -1;
  private int testItemCounter = -1;

  /**
   * Vytvori managera
   * 
   * @param trainInput
   * @param testInput
   *          soubor s testovacimi soubory.
   */
  public InputManager(File trainInput, File testInput) {
    this.trainInput = trainInput;
    this.testInput = testInput;
  }

  /**
   * Vytvori managera a inicializuje testovaci a trenovaci data
   * 
   * @param trainInput
   */
  public InputManager(File trainInput) {
    this.trainInput = trainInput;
    splitFile(trainInput);
  }

  /**
   * Otevre trenovaci soubor
   */
  public void initTrainData() {
    if (testInput != null) {
      trainData = processFile(trainInput);
    }
    resetTrainFile();
  }

  /**
   * Nastavi ukazatel na novy radek, aby se mohlo prochazet znova
   */
  public void resetTrainFile() {
    trainItemCounter = 0;
  }

  /**
   * zbytecna metoda
   */
  public void closeTrainFile() {
    trainData = null;
  }

  /**
   * @return dalsi DataItem nebo null, pokud je konec souboru
   */
  public DataItem getNextTrainItem() {
    return getNextItem(trainItemCounter++, trainData);
  }

  private DataItem getNextItem(int which, List<List<String>> from) {
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
    DataItem item = new DataItem(INPUT_LENGTH, OUTPUT_LENGTH);
    int i = 0;
    for (String stringValue : from.get(which)) {
      try {
        if (i < OUTPUT_LENGTH) {
          item.addOutputValue(Double.parseDouble(stringValue));
        } else {
          item.addInputValue(Double.parseDouble(stringValue));
        }
      } catch (NumberFormatException nfe) {
        System.err.println("Nepodarilo se provezt konverzi 'String -> Double' na retezci "
            + stringValue);
      }
      i++;
    }
    return item;
  }

  /**
   * Otevre testovaci soubor
   */
  public void initTestData() {
    if (testInput != null) {
      testData = processFile(testInput);
    }
    resetTestFile();
  }

  /**
   * Nastavi ukazatel na novy radek, aby se mohlo prochazet znova
   */
  public void resetTestFile() {
    testItemCounter = 0;
  }

  /**
   * zbytecna metoda
   */
  public void closeTestFile() {
    testData = null;
  }

  /**
   * @return dalsi DataItem nebo null, pokud je konec souboru
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
    List<List<String>> all = InputManager.processFile(inputFile);
    List<List<String>> testData = new ArrayList<List<String>>(128);
    List<List<String>> trainData = new ArrayList<List<String>>(128);
    int capacity = all.size();
    List<Integer> osudi = new ArrayList<Integer>(capacity);
    for (int i = 0; i < capacity; i++) {
      osudi.add(i); // naplni osudi cisly 0 az capacity
    }
    Collections.shuffle(osudi); // zamichej
    int i = 0;
    for (int index : osudi) { // prvni TRAIN_TEST_RATIO-inu dej do testovacich,
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

  private static List<List<String>> processFile(File file) {
    if (file == null) {
      throw new NullPointerException("file");
    }
    List<List<String>> data = new ArrayList<List<String>>(128);
    BufferedReader bis = null;
    try {
      bis = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      System.err.println("Soubor s daty nenalezen.");
      e.printStackTrace();
    }
    String line = null;
    try {
      while ((line = bis.readLine()) != null) {
        data.add(Arrays.asList(line.split(";")));
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

}
