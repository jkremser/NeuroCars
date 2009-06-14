package neurocars.gui.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import neurocars.entities.Car;

public interface ICarRenderer {

  public void draw(Graphics g, Car car, Color color);

  public void erase(Graphics g, Car car, BufferedImage background);
}
