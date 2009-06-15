package neurocars.controllers;

import neurocars.entities.Car;
import neurocars.neuralNetwork.Network;
import neurocars.neuralNetwork.service.Transformer;
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
  // private double maxSpeed;
  private Network net;
  private double threshold;

  public NeuroController(Network net, double threshold) {
    // net.setLearningMode(false);
    this.net = net;
    this.threshold = threshold;
  }

  public void next(Car car) {
    this.in = car.getNeuralNetworkInput();
    boolean flip = in.getCurveAngle() < 0;
    System.out.println(out);
    this.out = net.runNetwork(flip ? Transformer.flip(in) : in); // pretoc input
    this.out = (flip ? Transformer.flip(this.out) : this.out); // pretoc output
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
