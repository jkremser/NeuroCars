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
   * Zacatek sceny (inicializace, pred zpracovanim vstupu)
   */
  public void startScene();

  /**
   * Dokresleni zbytku sceny
   */
  public void finishScene();

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
