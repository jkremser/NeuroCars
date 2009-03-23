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
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import neurocars.Game;
import neurocars.entities.Car;
import neurocars.gui.sprites.CarSprite;
import neurocars.valueobj.Track;
import neurocars.valueobj.WayPoint;

import org.apache.log4j.Logger;

public class Java2DGUI implements IGUI {

  private static final Logger log = Logger.getLogger(Java2DGUI.class);

  // instance hry
  private final Game game;

  // titulek okna
  private static final String TITLE = "ne_uroc/ars";

  private boolean waitingForKeyPress = false;
  private BufferStrategy strategy;
  private GraphicsConfiguration gc;

  // mapa reprezentujici stisknute klavesy na klavesnici
  private final boolean[] keyboard = new boolean[65535];

  // pozadi hraci plochy
  private BufferedImage background;

  // zasobarna aut
  private Set<CarSprite> carSprites = new HashSet<CarSprite>();

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

    frame.setVisible(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.gui.IGUI#init()
   */
  public void init() {
    // background
    background = drawBackground(game.getTrack());

    Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
    g.drawImage(background, 0, 0, null);
    g.dispose();

    // carSprites
    for (Car c : game.getCars()) {
      carSprites.add(new CarSprite(c, Color.WHITE));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see neurocars.gui.IGUI#refresh()
   */
  public void refresh() {
    Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

    for (CarSprite cs : carSprites) {
      Car c = cs.getCar();

      // TODO: velikost prekreslovaneho pole
      int x1 = (int) (c.getX() - c.getVx() - 30);
      int y1 = (int) (c.getY() - c.getVy() - 30);
      int x2 = (int) (c.getX() - c.getVx() + 30);
      int y2 = (int) (c.getY() - c.getVy() + 30);
      g.drawImage(background, x1, y1, x2, y2, x1, y1, x2, y2, null);

      // g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
      // RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      cs.draw(g);
    }

    if (log.isDebugEnabled()) {
      this.drawStatusMessage(g);
    }

    // finally, we've completed drawing so clear up the graphics
    // and flip the buffer over
    g.dispose();
    strategy.show();
    // Toolkit.getDefaultToolkit().sync();
  }

  /**
   * Namaluje drahu jako obrazek na pozadi
   * 
   * @param track
   * @return
   */
  private BufferedImage drawBackground(Track track) {
    BufferedImage background = gc.createCompatibleImage(game.getXScreenSize(),
        game.getYScreenSize(), Transparency.OPAQUE);
    Graphics g = background.getGraphics();
    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, game.getXScreenSize(), game.getYScreenSize());

    for (int p = 0; p < track.getWayPoints().size(); p++) {
      WayPoint point = track.getWayPoints().get(p);
      g.setColor(Color.WHITE);
      g.drawOval((int) (point.getX() - point.getSize() / 2),
          (int) (point.getY() - point.getSize() / 2), point.getSize(),
          point.getSize());
      this.drawCentered(g, "" + p, (int) point.getX(), (int) point.getY());

      if (p > 0) {
        g.setColor(Color.DARK_GRAY);
        g.drawLine((int) track.getWayPoints().get(p - 1).getX(),
            (int) track.getWayPoints().get(p - 1).getY(), (int) point.getX(),
            (int) point.getY());
      }
    }
    g.dispose();
    return background;
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

  public boolean[] getKeyboard() {
    return keyboard;
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

}
