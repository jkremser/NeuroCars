package neurocars.controllers;

/**
 * Rozhrani ovladace vozidla
 * 
 * @author Lukas Holcik
 * 
 */
public interface IController {

  public boolean accelerate();

  public boolean brake();

  public boolean left();

  public boolean right();

}
