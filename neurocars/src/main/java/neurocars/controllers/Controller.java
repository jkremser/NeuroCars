package neurocars.controllers;

import neurocars.utils.ServiceException;

/**
 * Rozhrani ovladace vozidla
 * 
 * @author Lukas Holcik
 * 
 */
public abstract class Controller {

  public abstract void next() throws ServiceException;

  public abstract boolean accelerate();

  public abstract boolean brake();

  public abstract boolean left();

  public abstract boolean right();

  public String toString() {
    return (accelerate() ? 1 : 0) + ";" + (brake() ? 1 : 0) + ";"
        + (left() ? 1 : 0) + ";" + (right() ? 1 : 0);
  }

}
