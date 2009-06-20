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
  private boolean verbose;

  public NeuroController(Network net, double threshold, boolean verbose) {
    // net.setLearningMode(false);
    this.net = net;
    this.threshold = threshold;
    this.verbose = verbose;
  }

  public void next(Car car) {
    this.in = car.getNeuralNetworkInput();
    // boolean flip = in.getCurveAngle() < 0;
    boolean flip = false;
    if (verbose) {
      System.out.println(out);
    }
    if (flip) {
      this.out = Transformer.flip(net.runNetwork(Transformer.flip(in), verbose)); // pretoc
    } else {
      this.out = net.runNetwork(in, verbose);
    }
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
