package neurocars.neuralNetwork.service;

public class Constants {

  private static double learningConstant = 0.1;
  private static double momentum;

  public static void setLearningConstant(double learningConstant) {
    if (learningConstant <= 0 || learningConstant >= 1) {
      throw new IllegalArgumentException(
          "learning constant not in interval (0,1)");
    }
    Constants.learningConstant = learningConstant;
  }

  /**
   * Vrati ucici 'konstantu', ktera muze byt zavisla na iteraci
   * 
   * @param iteration
   *          iterace, ve ktere se uceni nachazi
   * @return
   */
  public static double getLearningConstant(int iteration) {
    return learningConstant + iteration / 250000.0;
  }

  public static void increaseLearningConstant() {
    learningConstant = learningConstant * 1.1;
  }

  public static void decreaseLearningConstant() {
    learningConstant = learningConstant * 0.9;
  }

  public static void setMomentum(double momentum) {
    if (momentum <= 0 || momentum >= 1) {
      throw new IllegalArgumentException("momentum not in interval (0,1)");
    }
    Constants.momentum = momentum;
  }

  public static double getMomentum() {
    return momentum;
  }
}
