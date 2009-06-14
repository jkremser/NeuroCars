package neurocars.neuralNetwork.service;

import neurocars.neuralNetwork.DataItem;
import neurocars.neuralNetwork.Network;
import neurocars.valueobj.NeuralNetworkInput;
import neurocars.valueobj.NeuralNetworkOutput;

/**
 * Transformuje hodnotu napr. pomoci sigmoidalni fce
 * 
 * @author Martin
 * 
 */
public class Transformer {

  public static final double E = 2.72;

  // TODO: dat e jako nejakou konstantu, nepotrebujeme presne e

  public static double sigmoidal(double value) {
    if (value < -8) {
      return 0.0;
    }
    if (value > 8) {
      return 1.0;
    }
    // double eToValue = 1 / java.lang.Math.exp(value);
    double eToValue = java.lang.Math.pow(E, -value);
    return 1.0 / (eToValue + 1.0);
    // kdo vi, jak je to efektivni.
    // Pokud by treba byla value normovana, byla by lepsi fce expm1
    // jenze to nikdy pravdepodobne kolem nuly vzdy nebude, takze nic
  }

  public static double tangens(double value) {
    if (value < -8) {
      return -1.0;
    }
    if (value > 8) {
      return 1.0;
    }
    double eToValue = java.lang.Math.pow(E, -value);
    return (1.0 - eToValue) / (1.0 + eToValue);
  }

  /**
   * Prevod mezi dvouma formaty podle specifikace Jestli se neco bude menit, tak
   * tady
   * 
   * @param input
   * @return
   */
  public static DataItem nnInputToDataItem(NeuralNetworkInput input) {
    DataItem item = new DataItem(Network.INPUT_SIZE);
    item.addInputValue(input.getSpeed());
    item.addInputValue(input.getSteeringWheel());
    item.addInputValue(input.getWayPointDistance());
    item.addInputValue(input.getWayPointAngle());
    item.addInputValue(input.getCurveAngle());
    // velikost nasledujiciho bodu. Ma to tam byt, nebo ne???
    return item;
  }

  /**
   * Prevod DataItem obsahujici jen vystup na NeuralNetworkOutput Pozor, takhle
   * muze vyjit brzda i plyn soucasne TODO: plyn-brzda pripadne doprava-doleva
   * vzdy prevezt na 1 neuron, jehoz vystup by se mapoval na -1/0/1. V tom
   * pripade je ale treba i zmena v datech ze souboru. To bude asi nejlehci
   * reseni. Jeste by se to mohlo menit pri nacitani ze souboru, ale zbytecne
   * komplikovane
   * 
   * @param outputDI
   * @return
   */
  public static NeuralNetworkOutput dataItemToNnOutput(DataItem outputDI) {
    NeuralNetworkOutput nnOutput = new NeuralNetworkOutput();

    nnOutput.setSpeed(outputDI.getOutput(0) * 2 - 1);
    nnOutput.setTurn(outputDI.getOutput(1) * 2 - 1);

    // plyn
    // if (closerToOne(outputDI.getOutput(0))) {
    // nnOutput.setAccelerate(true);
    // } else {
    // nnOutput.setAccelerate(false);
    // }
    // // brzda
    // if (closerToOne(outputDI.getOutput(1))) {
    // nnOutput.setBrake(true);
    // } else {
    // nnOutput.setBrake(false);
    // }
    //
    // // doleva
    // if (closerToOne(outputDI.getOutput(2))) {
    // nnOutput.setLeft(true);
    // } else {
    // nnOutput.setLeft(false);
    // }
    // // doprava
    // if (closerToOne(outputDI.getOutput(3))) {
    // nnOutput.setRight(true);
    // } else {
    // nnOutput.setRight(false);
    // }
    return nnOutput;
  }

  // /**
  // * Rozhodne, zda double hodnota je blize k 0 nebo 1
  // *
  // * @param value
  // * @return true pokud je blize k 1, false pokud je blize k 0
  // */
  // private static boolean closerToOne(double value) {
  // if (Math.abs(1 - value) < 0.68) { // vychytat
  // return true;
  // }
  // return false;
  // }

}
