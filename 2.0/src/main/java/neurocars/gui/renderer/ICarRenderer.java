package neurocars.gui.renderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import neurocars.entities.Car;

public interface ICarRenderer {

  public void draw(Graphics2D g, Car car, int color);

  public void erase(Graphics2D g, Car car, BufferedImage background);
}
