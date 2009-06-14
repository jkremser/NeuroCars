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

  /**
   * Zpozdeni cyklu, aby byla hra hratelna ... u NoGUI je 0
   * 
   * @return
   */
  public int getCycleDelay();

  /**
   * Byla stisknuta klavesa escape - pro ukonceni hry
   * 
   * @return
   */
  public boolean isEscapePressed();

}
