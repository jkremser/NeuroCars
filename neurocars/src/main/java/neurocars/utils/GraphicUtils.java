package neurocars.utils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * Graficke pomocne metody
 * 
 * @author Lukas Holcik
 * 
 */
public class GraphicUtils {

  /**
   * Rotuje obrazek o theta stupnu, vraci kopii
   * 
   * @param input
   * @param theta
   * @return
   */
  public static BufferedImage rotateImage(BufferedImage input, double theta) {
    // Graphics2D g = (Graphics2D) input.getGraphics();
    // g.drawImage(input, 0, 0, null);

    AffineTransform at = new AffineTransform();

    // rotate 45 degrees around image center
    at.rotate(theta, input.getWidth() / 2, input.getHeight() / 2);

    // translate to make sure the rotation doesn't cut off any image data
    // AffineTransform translationTransform;
    // translationTransform = findTranslation(at, input);
    // at.preConcatenate(translationTransform);

    BufferedImageOp bio = new AffineTransformOp(at,
        AffineTransformOp.TYPE_BILINEAR);

    return bio.filter(input, null);
  }

  // private static AffineTransform findTranslation(AffineTransform at,
  // BufferedImage bi) {
  // Point2D p2din, p2dout;
  //
  // p2din = new Point2D.Double(0.0, 0.0);
  // p2dout = at.transform(p2din, null);
  // double ytrans = p2dout.getY();
  //
  // p2din = new Point2D.Double(0, bi.getHeight());
  // p2dout = at.transform(p2din, null);
  // double xtrans = p2dout.getX();
  //
  // AffineTransform tat = new AffineTransform();
  // tat.translate(-xtrans, -ytrans);
  // return tat;
  // }

}
