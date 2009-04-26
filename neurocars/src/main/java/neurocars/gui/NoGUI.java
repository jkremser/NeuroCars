package neurocars.gui;

import neurocars.utils.ServiceException;

public class NoGUI implements IGUI {

  public NoGUI() {
    super();
  }

  public boolean[] getKeyboard() {
    return null;
  }

  public void init() throws ServiceException {
    System.out.println("Running without GUI...");
  }

  public void refresh() {
  }

  public int getCycleDelay() {
    return 0;
  }

}
