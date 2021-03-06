package neurocars.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import neurocars.entities.Car;
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

  private void reset() {
    accelerate = false;
    brake = false;
    left = false;
    right = false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.controllers.Controller#next()
   */
  public void next(Car car) throws ServiceException {
    try {
      if (car.isFinished()) {
        reset();
        return;
      }
      if (!replay.ready()) {
        return;
      }
      if ((row = replay.readLine()) != null) {
        String[] tokens = row.split(";");
        accelerate = "1".equals(tokens[0]);
        brake = "-1".equals(tokens[0]);
        left = "-1".equals(tokens[1]);
        right = "1".equals(tokens[1]);
      } else {
        reset();
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
