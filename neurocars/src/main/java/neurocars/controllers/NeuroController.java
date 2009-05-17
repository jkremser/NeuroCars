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

  public NeuroController(Network net, double threshold) {
    // net.setLearningMode(false);
    this.net = net;
    this.threshold = threshold;
  }

  public void next(Car car) {
    NeuralNetworkInput in = car.getNeuralNetworkInput();
    boolean flip = (in.getCurveAngle() < 0);
    if (flip) { // zaporny uhel zatacky, musime klopit
      in.setCurveAngle(-in.getCurveAngle());
      in.setWayPointAngle(-in.getWayPointAngle());
      in.setSteeringWheel(-in.getSteeringWheel());
    }
    this.out = net.runNetwork(in);
    if (flip) {
      out.setTurn(-out.getTurn());
    }
    System.out.println(out);
  }

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
