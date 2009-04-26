package neurocars.controllers;

import neurocars.entities.Car;
import neurocars.valueobj.NeuralNetworkOutput;

/**
 * Ovladac vozidla prostrednictvim neuronove site
 * 
 * @author Lukas Holcik
 */
public class NeuroController extends Controller {

  private Car car;
  private NeuralNetworkOutput out;

  public void next() {
    // NeuralNetworkInput in = car.getNeuralNetworkInput();

    // this.out = ...
  }

  public boolean accelerate() {
    return out.isAccelerate();
  }

  public boolean brake() {
    return out.isBrake();
  }

  public boolean left() {
    return out.isLeft();
  }

  public boolean right() {
    return out.isRight();
  }

  public void setCar(Car car) {
    this.car = car;
  }

  public Car getCar() {
    return car;
  }

}
