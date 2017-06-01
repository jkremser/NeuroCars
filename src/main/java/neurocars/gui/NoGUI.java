package neurocars.gui;

import neurocars.utils.ServiceException;

public class NoGUI implements IGUI {

  public NoGUI() {
    super();
  }

  public void init() throws ServiceException {
    System.out.println("Running without GUI...");
  }

  public void startScene() {
  }

  public void finishScene() {
  }

  public int getCycleDelay() {
    return 0;
  }

  public boolean isEscapePressed() {
    return false;
  }

}
