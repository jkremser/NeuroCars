package neurocars.services;

import neurocars.Game;
import neurocars.gui.IGUI;
import neurocars.gui.Java2DGUI;
import neurocars.gui.NoGUI;

public class GUIService extends ServiceBase {

  public static final String GUI_NO = "no";
  public static final String GUI_JAVA2D = "java2d";
  public static final String GUI_OPENGL = "opengl";
  private static GUIService instance = null;

  private GUIService() {
    super();
  }

  public static GUIService getInstance() {
    if (instance == null) {
      instance = new GUIService();
    }
    return instance;
  }

  /**
   * Vrati instanci GUI podle daneho identifikatoru
   * 
   * @param id
   * @param game
   * @return
   */
  public IGUI getGUI(String id, Game game) {
    if (GUI_JAVA2D.equals(id)) {
      return new Java2DGUI(game);
    }
    return new NoGUI();
  }
}
