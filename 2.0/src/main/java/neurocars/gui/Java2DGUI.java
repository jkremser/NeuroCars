package neurocars.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import neurocars.Game;
import neurocars.controllers.KeyboardController;
import neurocars.entities.Car;
import neurocars.gui.renderer.CarImageRenderer;
import neurocars.gui.renderer.ICarRenderer;
import neurocars.gui.sprites.CarSprite;
import neurocars.gui.sprites.ISprite;
import neurocars.utils.ServiceException;
import neurocars.valueobj.Track;
import neurocars.valueobj.WayPoint;

import org.apache.log4j.Logger;

/**
 * Java2d implementace GUI
 * 
 * @author Lukas Holcik
 * 
 */
public class Java2DGUI implements IGUI {

  private static final Logger log = Logger.getLogger(Java2DGUI.class);

  // delka herniho cyklu v ms
  private static final int CYCLE_DELAY = 55;

  // instance hry
  private final Game game;

  // titulek okna
  private static final String TITLE = "ne_uroc/ars";

  // Velikost okrajoveho pasu a stredovych sipek
  private static final int FlankWidth = 30, FlankHeight = 3, ArrowSize = 30;
  // zkoseni sipky (uhel v radianech)
  private static final double ArrowSkew = Math.PI / 4;

  private boolean waitingForKeyPress = false;
  private BufferStrategy strategy;
  private GraphicsConfiguration gc;

  // mapa reprezentujici stisknute klavesy na klavesnici
  private final boolean[] keyboard = new boolean[65535];

  private final Color[] playerColors = new Color[] { Color.WHITE, Color.BLUE,
      Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.ORANGE };

  // pozadi hraci plochy
  private BufferedImage background;

  // zasobarna aut
  private Set<ISprite> sprites = new HashSet<ISprite>();

  public Java2DGUI(Game game) {
    this.game = game;

    // Acquiring the current Graphics Device and Graphics Configuration
    GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice graphDevice = graphEnv.getDefaultScreenDevice();
    gc = graphDevice.getDefaultConfiguration();

    // create a frame to contain our game
    JFrame frame = new JFrame(gc);
    frame.setTitle(TITLE);

    // get hold the content of the frame and set up the resolution of the game
    JPanel panel = (JPanel) frame.getContentPane();
    panel.setPreferredSize(new Dimension(game.getXScreenSize(),
        game.getYScreenSize()));
    panel.setLayout(null);

    Canvas canvas = new Canvas();
    // setup our canvas size and put it into the content of the frame
    canvas.setBounds(0, 0, game.getXScreenSize(), game.getYScreenSize());
    panel.add(canvas);

    // Tell AWT not to bother repainting our canvas since we're
    // going to do that our self in accelerated mode
    canvas.setIgnoreRepaint(true);

    // finally make the window visible
    frame.pack();
    frame.setResizable(false);

    // add a listener to respond to the user closing the window. If they
    // do we'd like to exit the game
    frame.addWindowListener(new WindowAdapter() {

      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    // add a key input system (defined below) to our canvas
    // so we can respond to key pressed
    canvas.addKeyListener(new KeyInputHandler());

    // request the focus so key events come to us
    canvas.requestFocus();
    // frame.setFocusTraversalKeysEnabled(false);
    // canvas.setFocusTraversalKeysEnabled(false);

    // create the buffering strategy which will allow AWT
    // to manage our accelerated graphics
    canvas.createBufferStrategy(2);
    strategy = canvas.getBufferStrategy();
    System.out.println("Page-flipping strategy: "
        + strategy.getCapabilities().isPageFlipping());

    frame.setVisible(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.gui.IGUI#init()
   */
  public void init() throws ServiceException {
    // background
    background = drawBackground(game.getTrack());

    Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
    g.drawImage(background, 0, 0, null);
    g.dispose();

    // carSprites
    int color = 0;
    for (Car c : game.getCars()) {
      ICarRenderer renderer = new CarImageRenderer("car_" + (++color) + ".png");
      // ICarRenderer renderer = new CarCircleRenderer(playerColors[color++]);
      if (c.getController() instanceof KeyboardController) {
        KeyboardController kc = (KeyboardController) c.getController();
        kc.setKeyboard(keyboard);
      }
      sprites.add(new CarSprite(c, renderer));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.gui.IGUI#refresh()
   */
  public void refresh() {
    Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

    // napred vymazeme pozadi
    for (ISprite cs : sprites) {
      cs.erase(g, background);
    }

    // potom vykreslime auticka ...
    for (ISprite cs : sprites) {
      cs.draw(g);
    }

    if (log.isDebugEnabled()) {
      this.drawStatusMessage(g);
    }

    // finally, we've completed drawing so clear up the graphics
    // and flip the buffer over
    g.dispose();
    strategy.show();
  }

  /**
   * Namaluje drahu jako obrazek na pozadi
   * 
   * @param track
   * @return
   */
  private BufferedImage drawBackground(Track track) throws ServiceException {
    //
    // Vrstvy:
    // - kruhy
    // - stredove sipky
    // - okraj cesty
    // - cesta

    BufferedImage background = gc.createCompatibleImage(game.getXScreenSize(),
        game.getYScreenSize(), Transparency.OPAQUE);
    Graphics2D glroad = (Graphics2D) background.getGraphics();
    glroad.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    BufferedImage layer = gc.createCompatibleImage(game.getXScreenSize(),
        game.getYScreenSize(), Transparency.TRANSLUCENT);
    Graphics2D glborder = (Graphics2D) layer.getGraphics();
    glborder.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    BufferedImage layer2 = gc.createCompatibleImage(game.getXScreenSize(),
        game.getYScreenSize(), Transparency.TRANSLUCENT);
    Graphics2D glarrows = (Graphics2D) layer2.getGraphics();
    glarrows.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    BufferedImage layer3 = gc.createCompatibleImage(game.getXScreenSize(),
        game.getYScreenSize(), Transparency.TRANSLUCENT);
    Graphics2D glcircles = (Graphics2D) layer3.getGraphics();
    glcircles.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    // Pozadi
    glroad.setColor(new Color(0x316a31));
    glroad.setBackground(new Color(0x316a31));
    glroad.fillRect(0, 0, background.getWidth(), background.getHeight());

    this.drawTile(background, "noise2.png");

    for (int p = 0; p < track.getWayPoints().size(); p++) {
      WayPoint pt = track.getWayPoints().get(p);
      WayPoint ptp = track.getWayPoints().get(
          (p + 1) % track.getWayPoints().size());

      Ellipse2D.Double circle = new Ellipse2D.Double(
          (int) (pt.getX() - pt.getSize() / 2),
          (int) (pt.getY() - pt.getSize() / 2), pt.getSize(), pt.getSize());

      Point2D.Double dir = new Point2D.Double(ptp.getX() - pt.getX(),
          ptp.getY() - pt.getY());
      double len = dir.distance(0, 0);
      dir.setLocation(dir.getX() / len, dir.getY() / len);

      // Cesta
      int[] xRoad = new int[] {
          (int) (pt.getX() - dir.getY() * pt.getSize() / 2),
          (int) (pt.getX() + dir.getY() * pt.getSize() / 2),
          (int) (ptp.getX() + dir.getY() * ptp.getSize() / 2),
          (int) (ptp.getX() - dir.getY() * ptp.getSize() / 2) }, yRoad = new int[] {
          (int) (pt.getY() + dir.getX() * pt.getSize() / 2),
          (int) (pt.getY() - dir.getX() * pt.getSize() / 2),
          (int) (ptp.getY() - dir.getX() * ptp.getSize() / 2),
          (int) (ptp.getY() + dir.getX() * ptp.getSize() / 2) };

      glroad.setColor(Color.GRAY);
      glroad.fillPolygon(xRoad, yRoad, xRoad.length);
      glroad.fill(circle);

      // Kruhy
      glcircles.setColor(new Color(0.5f, 0.5f, 0.5f, 0.7f));
      glcircles.fill(circle);
      glcircles.setColor(Color.WHITE);
      glcircles.draw(circle);
      this.drawCentered(glcircles, "" + p, (int) pt.getX(), (int) pt.getY());

      // Stredove sipky
      glarrows.setColor(Color.YELLOW);
      double rest = len;
      // rest -= pt.getSize()/2 + ptp.getSize()/2;
      Point2D.Double arrpt = new Point2D.Double(pt.getX() + ArrowSize
          * dir.getX(), pt.getY() + ArrowSize * dir.getY());
      Point2D.Double skewL = new Point2D.Double((dir.getX()
          * Math.cos(ArrowSkew + Math.PI) - dir.getY()
          * Math.sin(ArrowSkew + Math.PI))
          * ArrowSize / 2,
          (dir.getX() * Math.sin(ArrowSkew + Math.PI) + dir.getY()
              * Math.cos(ArrowSkew + Math.PI))
              * ArrowSize / 2);
      Point2D.Double skewR = new Point2D.Double((dir.getX()
          * Math.cos(-ArrowSkew + Math.PI) - dir.getY()
          * Math.sin(-ArrowSkew + Math.PI))
          * ArrowSize / 2,
          (dir.getX() * Math.sin(-ArrowSkew + Math.PI) + dir.getY()
              * Math.cos(-ArrowSkew + Math.PI))
              * ArrowSize / 2);

      while (rest >= ArrowSize) {

        glarrows.fillPolygon(new int[] { (int) (arrpt.getX()),
            (int) (arrpt.getX() + skewL.getX()),
            (int) (arrpt.getX() + skewL.getX() - dir.getX() * ArrowSize / 2),
            (int) (arrpt.getX() - dir.getX() * ArrowSize / 2),
            (int) (arrpt.getX() + skewR.getX() - dir.getX() * ArrowSize / 2),
            (int) (arrpt.getX() + skewR.getX()), }, new int[] {
            (int) (arrpt.getY()), (int) (arrpt.getY() + skewL.getY()),
            (int) (arrpt.getY() + skewL.getY() - dir.getY() * ArrowSize / 2),
            (int) (arrpt.getY() - dir.getY() * ArrowSize / 2),
            (int) (arrpt.getY() + skewR.getY() - dir.getY() * ArrowSize / 2),
            (int) (arrpt.getY() + skewR.getY()), }, 6);

        rest -= 2 * ArrowSize;
        arrpt.setLocation(arrpt.getX() + dir.getX() * 2 * ArrowSize,
            arrpt.getY() + dir.getY() * 2 * ArrowSize);
      }

      Point2D.Double flnkptL = new Point2D.Double(pt.getX() + dir.getY()
          * pt.getSize() / 2, pt.getY() - dir.getX() * pt.getSize() / 2);

      Point2D.Double flnkptR = new Point2D.Double(pt.getX() - dir.getY()
          * pt.getSize() / 2, pt.getY() + dir.getX() * pt.getSize() / 2);

      // smerove vektory k dalsimu bodu po okraji (pac velikost bodu muze byt
      // ruzna)
      Point2D.Double dirL = new Point2D.Double((ptp.getX() + dir.getY()
          * ptp.getSize() / 2)
          - (pt.getX() + dir.getY() * pt.getSize() / 2),
          (ptp.getY() - dir.getX() * ptp.getSize() / 2)
              - (pt.getY() - dir.getX() * pt.getSize() / 2));
      double lenL = dirL.distance(0, 0);
      dirL.setLocation(dirL.getX() / lenL, dirL.getY() / lenL);

      Point2D.Double dirR = new Point2D.Double((ptp.getX() - dir.getY()
          * ptp.getSize() / 2)
          - (pt.getX() - dir.getY() * pt.getSize() / 2),
          (ptp.getY() + dir.getX() * ptp.getSize() / 2)
              - (pt.getY() + dir.getX() * pt.getSize() / 2));
      double lenR = dirR.distance(0, 0);
      dirR.setLocation(dirR.getX() / lenR, dirR.getY() / lenR);

      // Okraj cesty
      glborder.setColor(Color.WHITE);

      rest = lenL; // (melo by lenL == lenR)
      while (rest > 0) {
        double size = (rest < FlankWidth) ? rest : FlankWidth;

        glborder.setColor(Color.WHITE);
        glborder.fillPolygon(
            new int[] {
                (int) (flnkptL.getX() + dir.getY() * FlankHeight / 2),
                (int) (flnkptL.getX() + dir.getY() * FlankHeight / 2 + dirL.getX()
                    * size),
                (int) (flnkptL.getX() - dir.getY() * FlankHeight / 2 + dirL.getX()
                    * size),
                (int) (flnkptL.getX() - dir.getY() * FlankHeight / 2), },
            new int[] {
                (int) (flnkptL.getY() - dir.getX() * FlankHeight / 2),
                (int) (flnkptL.getY() - dir.getX() * FlankHeight / 2 + dirL.getY()
                    * size),
                (int) (flnkptL.getY() + dir.getX() * FlankHeight / 2 + dirL.getY()
                    * size),
                (int) (flnkptL.getY() + dir.getX() * FlankHeight / 2), }, 4);

        glborder.setColor(Color.CYAN);
        glborder.fillPolygon(
            new int[] {
                (int) (flnkptR.getX() + dir.getY() * FlankHeight / 2),
                (int) (flnkptR.getX() + dir.getY() * FlankHeight / 2 + dirR.getX()
                    * size),
                (int) (flnkptR.getX() - dir.getY() * FlankHeight / 2 + dirR.getX()
                    * size),
                (int) (flnkptR.getX() - dir.getY() * FlankHeight / 2), },
            new int[] {
                (int) (flnkptR.getY() - dir.getX() * FlankHeight / 2),
                (int) (flnkptR.getY() - dir.getX() * FlankHeight / 2 + dirR.getY()
                    * size),
                (int) (flnkptR.getY() + dir.getX() * FlankHeight / 2 + dirR.getY()
                    * size),
                (int) (flnkptR.getY() + dir.getX() * FlankHeight / 2), }, 4);

        rest -= 1.5 * FlankWidth;
        flnkptL.setLocation(flnkptL.getX() + dirL.getX() * 1.5 * FlankWidth,
            flnkptL.getY() + dirL.getY() * 1.5 * FlankWidth);
        flnkptR.setLocation(flnkptR.getX() + dirR.getX() * 1.5 * FlankWidth,
            flnkptR.getY() + dirR.getY() * 1.5 * FlankWidth);
      }
    }

    glroad.drawRenderedImage(layer, new AffineTransform());
    glroad.drawRenderedImage(layer2, new AffineTransform());
    glroad.drawRenderedImage(layer3, new AffineTransform());

    this.drawTile(background, "noise.png");

    glroad.dispose();
    glborder.dispose();
    glarrows.dispose();
    glcircles.dispose();

    return background;
  }

  private void drawTile(BufferedImage img, String file) throws ServiceException {
    java.io.InputStream is = this.getClass()
        .getClassLoader()
        .getResourceAsStream(file);
    BufferedImage tile = null;
    try {
      tile = javax.imageio.ImageIO.read(is);
    } catch (java.io.IOException e) {
      throw new ServiceException(e);
    }

    Graphics2D g = (Graphics2D) img.getGraphics();
    int height = tile.getHeight(), width = tile.getWidth(), m = img.getWidth(), n = img.getHeight();
    m = (m % width == 0) ? (m / width) : (m / width + 1);
    n = (n % height == 0) ? (n / height) : (n / height + 1);
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        g.drawImage(tile, new AffineTransform(1f, 0f, 0f, 1f, i * width, j
            * height), null);
      }
    }
  }

  /**
   * Vypise
   * 
   * @param g
   * @param background
   */
  private void drawStatusMessage(Graphics g) {
    int x1 = 10;
    int y1 = game.getYScreenSize() - 20;
    int x2 = x1 + g.getFontMetrics().stringWidth(game.getStatusMessage()) + 50;
    int y2 = game.getYScreenSize();

    // TODO: tady zrejme patri fillRect() nebo nic
    g.drawImage(background, x1, y1, x2, y2, x1, y1, x2, y2, null);
    g.setColor(Color.WHITE);
    g.drawString(game.getStatusMessage(), x1, y1 + 10);
  }

  /**
   * Napise text vycentrovany do daneho bodu
   * 
   * @param g
   * @param message
   * @param x
   * @param y
   */
  public void drawCentered(Graphics g, String message, int x, int y) {
    int h = g.getFontMetrics().getHeight();
    int w = g.getFontMetrics().stringWidth(message);
    g.drawString(message, x - w / 2, y - h / 2);
  }

  public boolean isEscapePressed() {
    return keyboard[KeyEvent.VK_ESCAPE];
  }

  /**
   * A class to handle keyboard input from the user. The class handles both
   * dynamic input during game play, i.e. left/right and shoot, and more static
   * type input (i.e. press any key to continue)
   * 
   * This has been implemented as an inner class more through habbit then
   * anything else. Its perfectly normal to implement this as seperate class if
   * slight less convienient.
   * 
   * @author Kevin Glass
   */
  private class KeyInputHandler extends KeyAdapter {

    /** The number of key presses we've had while waiting for an "any key" press */
    private int pressCount = 1;

    /**
     * Notification from AWT that a key has been pressed. Note that a key being
     * pressed is equal to being pushed down but *NOT* released. Thats where
     * keyTyped() comes in.
     * 
     * @param e
     *          The details of the key that was pressed
     */
    public void keyPressed(KeyEvent e) {
      // if we're waiting for an "any key" typed then we don't
      // want to do anything with just a "press"
      if (waitingForKeyPress) {
        return;
      }

      if (log.isTraceEnabled()) {
        log.trace("Key pressed: " + e.getKeyCode());
      }

      keyboard[e.getKeyCode()] = true;
    }

    /**
     * Notification from AWT that a key has been released.
     * 
     * @param e
     *          The details of the key that was released
     */
    public void keyReleased(KeyEvent e) {
      // if we're waiting for an "any key" typed then we don't
      // want to do anything with just a "released"
      if (waitingForKeyPress) {
        return;
      }

      if (log.isTraceEnabled()) {
        log.trace("Key released: " + e.getKeyCode());
      }

      keyboard[e.getKeyCode()] = false;
    }

    /**
     * Notification from AWT that a key has been typed. Note that typing a key
     * means to both press and then release it.
     * 
     * @param e
     *          The details of the key that was typed.
     */
    public void keyTyped(KeyEvent e) {
      // if we're waiting for a "any key" type then
      // check if we've recieved any recently. We may

      // have had a keyType() event from the user releasing

      // the shoot or move keys, hence the use of the "pressCount"
      // counter.

      if (waitingForKeyPress) {
        if (pressCount == 1) {
          if (log.isDebugEnabled()) {
            log.debug("Key pressed!");
          }
          // since we've now recieved our key typed

          // event we can mark it as such and start

          // our new game
          waitingForKeyPress = false;
          // startGame();
          pressCount = 0;
        } else {
          pressCount++;
        }
      }

      // if we hit escape, then quit the game

      // if (e.getKeyChar() == 27) {
      // System.exit(0);
      // }
    }
  }

  public int getCycleDelay() {
    return CYCLE_DELAY;
  }

}
// vim: sw=2
