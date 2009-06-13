package neurocars.neuralNetwork.service;

import java.util.ArrayList;
import java.util.List;

import neurocars.neuralNetwork.DataItem;
import neurocars.neuralNetwork.Network;

public class InputManagerXorImpl implements InputManager {

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
    trainData = new ArrayList<DataItem>(20);
    fillTrainData();
    trainCounter = 0;
  }

  public void resetTestData() {
    // TODO Auto-generated method stub

  }

  public void resetTrainData() {
    trainCounter = 0;
  }

  private void fillTrainData() {
    DataItem item;

    // 1;0;1;0; 4.0;-0.05;236.061458;0.017661;-0.692523;
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(0);

    item.addOutputValue(0);
    trainData.add(item);

    // 1;0;0;0;4.2;0.0;231.862121;0.017885;-0.692747;
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(1);

    item.addOutputValue(1);
    trainData.add(item);

    // 1;0;0;0;4.4;0.0;227.462835;0.018125;-0.692987;
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(1);

    item.addOutputValue(1);
    trainData.add(item);

    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(1);

    item.addOutputValue(1);
    trainData.add(item);
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(0);

    item.addOutputValue(0);
    trainData.add(item);
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(0);

    item.addOutputValue(0);
    trainData.add(item);
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(0);

    item.addOutputValue(0);
    trainData.add(item);
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(1);

    item.addOutputValue(1);
    trainData.add(item);
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(0);

    item.addOutputValue(0);
    trainData.add(item);
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(1);

    item.addOutputValue(0);
    trainData.add(item);
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(0);

    item.addOutputValue(1);
    trainData.add(item);
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(1);

    item.addOutputValue(0);
    trainData.add(item);
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(0);

    item.addOutputValue(0);
    trainData.add(item);
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(0);

    item.addOutputValue(1);
    trainData.add(item);
    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(1);

    item.addOutputValue(1);
    trainData.add(item);

    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(0);

    item.addOutputValue(1);
    trainData.add(item);

    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(0);

    item.addOutputValue(0);
    trainData.add(item);

    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(0);
    item.addInputValue(1);

    item.addOutputValue(0);
    trainData.add(item);

    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(1);

    item.addOutputValue(0);
    trainData.add(item);

    item = new DataItem(Network.INPUT_SIZE, Network.OUTPUT_SIZE);
    item.addInputValue(0);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(1);
    item.addInputValue(1);

    item.addOutputValue(0);
    trainData.add(item);
  }

  public boolean containsTestData() {
    return false;
  }

}
