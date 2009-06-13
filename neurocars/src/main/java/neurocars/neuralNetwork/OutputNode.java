package neurocars.neuralNetwork;

import java.io.Serializable;

import neurocars.neuralNetwork.service.Transformer;

public class OutputNode implements Serializable {

  private static final long serialVersionUID = -553776070091495334L;
  private double weightedInputSum = 0.0;
  private double output;
  private transient double error;

  public void initWeightedInputSum() {
    weightedInputSum = 0.0;
  }

  public void addWeightedInput(double weightedInput) {
    this.weightedInputSum += weightedInput;
  }

  public double getOutput() {
    return output;
  }

  public void setOutput(double output) {
    this.output = output;
  }

  /**
   * Vypocita svoji chybu
   * 
   * @param requiredOutput
   *          hodnota pozadovaneho vystupu
   */
  public void computeError(double requiredOutput) {
    error = output * (1 - output) * (requiredOutput - output);
  }

  public double getError() {
    return error;
  }

  public void computeOutput() {
    output = Transformer.sigmoidal(weightedInputSum);
  }

}
