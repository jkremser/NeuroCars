package neurocars.utils;

/**
 * Standardni programova vyjimka
 * 
 * @author Lukas Holcik
 * 
 */
public class ServiceException extends Exception {

  public ServiceException(String arg0) {
    super(arg0);
  }

  public ServiceException(Throwable arg0) {
    super(arg0);
  }

  public ServiceException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
