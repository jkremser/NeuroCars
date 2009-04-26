package neurocars.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import neurocars.utils.ServiceException;

/**
 * Controller nacitajici vstup z replay souboru
 * 
 * @author Lukas Holcik
 * 
 */
public class ReplayController extends Controller {

  private BufferedReader replay;
  private String row;
  private boolean accelerate;
  private boolean brake;
  private boolean left;
  private boolean right;

  public ReplayController(String replayFile) throws ServiceException {
    super();
    try {
      replay = new BufferedReader(new FileReader(replayFile));
      replay.readLine(); // nacte a zahodi prvni radek - hlavicku
    } catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.controllers.Controller#next()
   */
  public void next() throws ServiceException {
    try {
      if (!replay.ready()) {
        return;
      }
      if ((row = replay.readLine()) != null) {
        String[] tokens = row.split(";");
        accelerate = !"0".equals(tokens[0]);
        brake = !"0".equals(tokens[1]);
        left = !"0".equals(tokens[2]);
        right = !"0".equals(tokens[3]);
      } else {
        accelerate = false;
        brake = false;
        left = false;
        right = false;
        replay.close();
      }
    } catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.controllers.Controller#accelerate()
   */
  public boolean accelerate() {
    return accelerate;
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.controllers.Controller#brake()
   */
  public boolean brake() {
    return brake;
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.controllers.Controller#left()
   */
  public boolean left() {
    return left;
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.controllers.Controller#right()
   */
  public boolean right() {
    return right;
  }

}
