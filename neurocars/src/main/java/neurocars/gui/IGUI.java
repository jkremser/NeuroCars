package neurocars.gui;

import neurocars.utils.ServiceException;

public interface IGUI {

  public void init() throws ServiceException;

  public void refresh();

  public boolean[] getKeyboard();

}
