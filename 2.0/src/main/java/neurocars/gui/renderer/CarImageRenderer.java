package neurocars.gui.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import neurocars.entities.Car;
import neurocars.utils.GraphicUtils;
import neurocars.utils.ServiceException;

/**
 * Jednoduchy graficky renderer auta - zobrazuje otoceny obrazek
 * 
 * @author Lukas Holcik
 * 
 */
public class CarImageRenderer implements ICarRenderer {

  private static BufferedImage[] images = new BufferedImage[72];

  public CarImageRenderer(String image) throws ServiceException {
    try {
      InputStream is = this.getClass().getClassLoader().getResourceAsStream(
          image);
      BufferedImage car = ImageIO.read(is);
      images[0] = car;
      for (int a = 1; a < 72; a++) {
        if (a < 72) {
          images[a] = GraphicUtils.rotateImage(car, Math.toRadians(a * 5));
        } else {
          images[a] = car;
        }
      }
    } catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.gui.renderer.ICarRenderer#draw(java.awt.Graphics,
   * neurocars.entities.Car, java.awt.Color)
   */
  public void draw(Graphics g, Car car) {
    int angle = (int) Math.toDegrees(car.getAngle()) / 5;
    BufferedImage image = images[angle];

    g.drawImage(image, (int) (car.getX() - image.getWidth() / 2),
        (int) (car.getY() - image.getHeight() / 2), null);
  }

  public void erase(Graphics g, Car car, BufferedImage background) {
    int w = images[0].getWidth() / 2 + 3;
    int h = images[0].getHeight() / 2 + 3;
    int x1 = (int) (car.getX() - car.getVx() - w);
    int y1 = (int) (car.getY() - car.getVy() - h);
    int x2 = (int) (car.getX() - car.getVx() + w);
    int y2 = (int) (car.getY() - car.getVy() + h);
    g.drawImage(background, x1, y1, x2, y2, x1, y1, x2, y2, null);
  }
}
