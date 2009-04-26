package neurocars.services;

public class GameService extends ServiceBase {

  private static GameService instance = null;

  private GameService() {
    super();
  }

  public static GameService getInstance() {
    if (instance == null) {
      instance = new GameService();
    }
    return instance;
  }

}
