package neurocars.controllers;

import neurocars.entities.Car;

/**
 * Klavesnicovy ovladac zavodniho auta
 * 
 * @author Lukas Holcik
 * 
 */
public class KeyboardController extends Controller {

  private boolean[] keyboard;
  private final int accelerate;
  private final int brake;
  private final int right;
  private final int left;

  public KeyboardController(int accelerate, int brake, int left, int right) {
    super();
    this.accelerate = accelerate;
    this.brake = brake;
    this.right = right;
    this.left = left;
  }

  public void setKeyboard(boolean[] keyboard) {
    this.keyboard = keyboard;
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.controllers.Controller#next()
   */
  public void next(Car car) {
    // NOOP
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.controllers.Controller#accelerate()
   */
  public boolean accelerate() {
    return keyboard[this.accelerate];
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.controllers.Controller#brake()
   */
  public boolean brake() {
    return keyboard[this.brake];
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.controllers.Controller#left()
   */
  public boolean left() {
    return keyboard[this.left];
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.controllers.Controller#right()
   */
  public boolean right() {
    return keyboard[this.right];
  }

}
