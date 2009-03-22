package neurocars.controllers;

/**
 * Klavesnicovy ovladac zavodniho auta
 * 
 * @author Lukas Holcik
 * 
 */
public class KeyboardController implements IController {

  private final boolean[] keyCodes;
  private final int accelerate;
  private final int brake;
  private final int right;
  private final int left;

  public KeyboardController(boolean[] keyCodes, int accelerate, int brake,
      int right, int left) {
    super();
    this.keyCodes = keyCodes;
    this.accelerate = accelerate;
    this.brake = brake;
    this.right = right;
    this.left = left;
  }

  public boolean accelerate() {
    return keyCodes[this.accelerate];
  }

  public boolean brake() {
    return keyCodes[this.brake];
  }

  public boolean left() {
    return keyCodes[this.left];
  }

  public boolean right() {
    return keyCodes[this.right];
  }

}
