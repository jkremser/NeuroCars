package neurocars.gui;

import neurocars.Game;
import neurocars.utils.ServiceException;

public class NoGUI implements IGUI {

  // private final Game game;

  public NoGUI(Game game) {
    // this.game = game;
  }

  public boolean[] getKeyboard() {
    return null;
  }

  public void init() throws ServiceException {
    System.out.println("Running without GUI...");
  }

  public void refresh() {

  }

}
