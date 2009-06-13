package neurocars.neuralNetwork.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import neurocars.neuralNetwork.DataItem;

/**
 * realizes function f(x,y) = x! + y
 * 
 * @author freon
 * 
 */
public class InputManagerFunctionlImpl implements InputManager {

  private List<DataItem> trainData;
  private int trainCounter;

  public DataItem getNextTestItem() {
    // TODO Auto-generated method stub
    return null;
  }

  public DataItem getNextTrainItem() {
    if (trainCounter < trainData.size()) {
      return trainData.get(trainCounter++);
    }
    return null;
  }

  public void initTestData() {
    // TODO Auto-generated method stub

  }

  public void initTrainData() {
    trainData = new ArrayList<DataItem>(1024);
    fillTrainData();
    trainCounter = 0;
  }

  public void resetTestData() {
    // TODO Auto-generated method stub

  }

  public void resetTrainData() {
    trainCounter = 0;
  }

  private int fce(int a, int b) {
    return ((a - 1 == b) ? 1 : 0);
  }

  private void fillTrainData() {
    for (int i = 0; i < 100; i++) {
      Random r = new Random();
      int a = r.nextInt(20);
      int b = r.nextInt(20);
      DataItem item = new DataItem(2, 1);
      item.addInputValue(a);
      item.addInputValue(b);
      item.addOutputValue(fce(a, b));
      System.out.println(item);
      trainData.add(item);
    }

  }

  public boolean containsTestData() {
    return false;
  }

}
