package neurocars.gui.renderer;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

  private static BufferedImage[][] images = new BufferedImage[GraphicUtils.palette.length][72];

  public CarImageRenderer() throws ServiceException {
    try {
      InputStream isc = this.getClass().getClassLoader().getResourceAsStream(
          "car_template2.png");
      // "car2_small.png");
      BufferedImage car = ImageIO.read(isc);
      InputStream ism = this.getClass().getClassLoader().getResourceAsStream(
          "car_outline.png");
      BufferedImage mask = ImageIO.read(ism);

      for (int a = 0; a < 72; a++) {
        BufferedImage rcar = null;
        BufferedImage rmask = null;
        if (a > 0) {
          rcar = GraphicUtils.rotateImage(car, Math.toRadians(a * 5));
          rmask = GraphicUtils.rotateImage(mask, Math.toRadians(a * 5));
        } else {
          rcar = car;
          rmask = mask;
        }

        int w = rcar.getWidth(), h = rcar.getHeight();
        if ((rmask.getWidth() != w) || (rmask.getHeight() != h)) {
          throw new ServiceException("Mask size does not match sprite size");
        }

        Graphics2D g = (Graphics2D) rcar.createGraphics();
        java.awt.GraphicsConfiguration gc = g.getDeviceConfiguration();

        int[] spixels = new int[w * h];
        int[] mpixels = new int[w * h];
        rmask.getRGB(0, 0, w, h, mpixels, 0, w);

        for (int i = 0; i < GraphicUtils.palette.length; i++) {
          images[i][a] = gc.createCompatibleImage(w, h,
              java.awt.Transparency.TRANSLUCENT);
          BufferedImage sprite = gc.createCompatibleImage(w, h,
              java.awt.Transparency.TRANSLUCENT);
          Graphics2D gsprite = (Graphics2D) sprite.getGraphics();
          gsprite.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);

          gsprite.setColor(GraphicUtils.palette[i]);
          gsprite.setBackground(GraphicUtils.palette[i]);
          gsprite.fillRect(0, 0, w, h);
          gsprite.drawImage(rcar, 0, 0, null);
          gsprite.dispose();

          sprite.getRGB(0, 0, w, h, spixels, 0, w);
          for (int j = 0; j < spixels.length; j++) {
            spixels[j] = (spixels[j] & 0x00ffffff)
                | ((mpixels[j] & 0xff) << 24);
          }
          sprite.setRGB(0, 0, w, h, spixels, 0, w);

          images[i][a].getGraphics().drawImage(sprite, 0, 0, null);
        }
        g.dispose();
      }
    } catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.gui.renderer.ICarRenderer#draw(java.awt.Graphics2D,
   * neurocars.entities.Car, int)
   */
  public void draw(Graphics2D g, Car car, int color) {
    int angle = (int) Math.toDegrees(car.getAngle()) / 5;

    BufferedImage image = images[color][angle];

    g.drawImage(image, (int) (car.getX() - image.getWidth() / 2),
        (int) (car.getY() - image.getHeight() / 2), null);
  }

  public void erase(Graphics2D g, Car car, BufferedImage background) {
    int angle = (int) Math.toDegrees(car.getAngle()) / 5;
    BufferedImage image = images[0][angle];
    int w = image.getWidth(), h = image.getHeight(), x1 = (int) (car.getX() - w / 2), y1 = (int) (car.getY() - h / 2), x2 = x1
        + w, y2 = y1 + h;
    g.drawImage(background, x1, y1, x2, y2, x1, y1, x2, y2, null);
  }
}
