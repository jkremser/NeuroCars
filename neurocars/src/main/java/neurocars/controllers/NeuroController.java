package neurocars.controllers;

import neurocars.entities.Car;
import neurocars.neuralNetwork.Network;
import neurocars.valueobj.NeuralNetworkInput;
import neurocars.valueobj.NeuralNetworkOutput;

/**
 * Ovladac vozidla prostrednictvim neuronove site
 * 
 * @author Lukas Holcik
 */
public class NeuroController extends Controller {

  private NeuralNetworkInput in;
  private NeuralNetworkOutput out;
  private Network net;

  // private double threshold;

  // private static final double PROBABILITY = 0.9;

  public NeuroController(Network net) {
    // net.setLearningMode(false);
    this.net = net;
    // this.threshold = threshold;
  }

  public void next(Car car) {
    this.in = car.getNeuralNetworkInput();

    this.out = net.runNetwork(in);
  }

  // public boolean accelerate() {
  // double randValue = new Random().nextDouble();
  // double difference = out.getSpeed() - threshold;
  // boolean retValue;
  // if (out.getSpeed() > threshold) {
  // retValue = randValue < PROBABILITY + difference;
  // } else {
  // retValue = randValue + difference > PROBABILITY;
  // }
  // return retValue;
  // }
  //
  // public boolean brake() {
  // double randValue = new Random().nextDouble();
  // double difference = -out.getSpeed() - threshold;
  // boolean retValue;
  // if (out.getSpeed() < -threshold) {
  // retValue = randValue < PROBABILITY + difference;
  // } else {
  // retValue = randValue + difference > PROBABILITY;
  // }
  // return retValue;
  // }
  //
  // public boolean left() {
  // double randValue = new Random().nextDouble();
  // double difference = -out.getTurn() - threshold;
  // boolean retValue;
  // if (out.getTurn() < -threshold) {
  // retValue = randValue < PROBABILITY + difference;
  // } else {
  // retValue = randValue + difference > PROBABILITY;
  // }
  // return retValue;
  // }
  //
  // public boolean right() {
  // double randValue = new Random().nextDouble();
  // double difference = out.getTurn() - threshold;
  // boolean retValue;
  // if (out.getTurn() > threshold) {
  // retValue = randValue < PROBABILITY + difference;
  // } else {
  // retValue = randValue + difference > PROBABILITY;
  // }
  // return retValue;
  // }

  public boolean accelerate() {
    return out.getSpeed() > in.getSpeed();
  }

  public boolean brake() {
    return out.getSpeed() < in.getSpeed();
  }

  public boolean left() {
    return out.getTurn() < in.getWayPointAngle();
  }

  public boolean right() {
    return out.getTurn() > in.getWayPointAngle();
  }

}
