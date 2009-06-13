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

  private NeuralNetworkOutput out;
  private Network net;
  private double threshold;

  // private static final double PROBABILITY = 0.9;

  public NeuroController(Network net, double threshold) {
    // net.setLearningMode(false);
    this.net = net;
    this.threshold = threshold;
  }

  public void next(Car car) {
    NeuralNetworkInput in = car.getNeuralNetworkInput();
    // boolean flip = (in.getCurveAngle() < -0.01);
    // if (flip) { // zaporny uhel zatacky, musime klopit
    // in.setCurveAngle(-in.getCurveAngle());
    // in.setWayPointAngle(-in.getWayPointAngle());
    // in.setSteeringWheel(-in.getSteeringWheel());
    // }
    // zatacka hack
    // if ((in.getCurveAngle() < -0.01 && out.getTurn() > 0.01)
    // || (in.getCurveAngle() > -0.01 && out.getTurn() < 0.01)) {
    // out.setTurn(-out.getTurn());
    // }

    this.out = net.runNetwork(in);
    // if (flip) {
    // out.setTurn(-out.getTurn());
    // }
    System.out.println(out);
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
    return out.getSpeed() > threshold;
  }

  public boolean brake() {
    return out.getSpeed() < -threshold;
  }

  public boolean left() {
    return out.getTurn() < -threshold;
  }

  public boolean right() {
    return out.getTurn() > threshold;
  }

}
