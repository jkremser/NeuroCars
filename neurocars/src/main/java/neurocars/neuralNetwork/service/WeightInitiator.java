package neurocars.neuralNetwork.service;

import java.util.Random;

public class WeightInitiator {

  private Random generator = new Random();

  /**
   * 
   * @return hodnota (-0.5;0.5>
   */
  public Double getWeight() {
    return Double.valueOf((generator.nextDouble()) - 0.5);
  }

}