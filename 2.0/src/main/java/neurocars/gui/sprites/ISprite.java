package neurocars.gui.sprites;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Swingove graficke rozhrani hernich objektu
 * 
 * @author Lukas Holcik
 * 
 */
public interface ISprite {

  public void draw(Graphics g);

  public void erase(Graphics g, BufferedImage background);

}
