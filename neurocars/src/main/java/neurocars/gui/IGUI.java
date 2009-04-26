package neurocars.gui;

import neurocars.utils.ServiceException;

public interface IGUI {

  /**
   * Inicializuje graficke prostredi
   * 
   * @throws ServiceException
   */
  public void init() throws ServiceException;

  /**
   * Prekresli obrazovku
   */
  public void refresh();

  public boolean[] getKeyboard();

  /**
   * Zpozdeni cyklu, aby byla hra hratelna ... u NoGUI je 0
   * 
   * @return
   */
  public int getCycleDelay();

}
