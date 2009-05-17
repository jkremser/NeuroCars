package neurocars;

import java.io.File;

import neurocars.neuralNetwork.Network;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Main {

  public static void requiredParameter(CommandLine line, String name,
      Options options) {
    if (!line.hasOption(name)) {
      System.out.println("Required parameter missing: " + name);
      printHelp(options);
      System.exit(1);
    }
  }

  public static void printHelp(Options options) {
    HelpFormatter help = new HelpFormatter();
    help.printHelp("neurocars", options, true);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    Options options = new Options();
    options.addOption("mode", true, "program mode [train/race]");
    options.addOption("scenario", true, "game scenario file [race mode]");
    options.addOption("input", true,
        "neural network training input file [train mode]");
    options.addOption("output", true,
        "serialized neural network output file [train mode]");
    options.addOption("threshold", true, "error threshold [train mode]");
    options.addOption("layers", true, "hidden layers count [train mode]");
    options.addOption("neurons", true,
        "number of neurons in every hidden layer [train mode]");
    options.addOption("learningconstant", true,
        "learning constant [train mode]");
    options.addOption("iterations", true,
        "maximum iterations count [train mode]");
    options.addOption("help", false, "print this help");

    CommandLineParser parser = new PosixParser();

    try {
      // parse the command line arguments
      CommandLine line = parser.parse(options, args);

      if (line.hasOption("help")) {
        printHelp(options);
        System.exit(0);
      }

      requiredParameter(line, "mode", options);
      // print the value of block-size
      String mode = line.getOptionValue("mode");

      if ("train".equals(mode)) {
        requiredParameter(line, "input", options);
        requiredParameter(line, "output", options);

        File inputFile = new File(line.getOptionValue("input"));
        // TODO: jak zadat vstup?
        File outputFile = new File(line.getOptionValue("output"));
        double thresholdError = Double.valueOf(line.getOptionValue("threshold",
            "0.5"));
        int hiddenLayersNumber = Integer.valueOf(line.getOptionValue("layers",
            "1"));
        int hiddenLayerNeurons = Integer.valueOf(line.getOptionValue("neurons",
            "10"));
        double learningConstant = Double.valueOf(line.getOptionValue(
            "learningconstant", "0.5"));
        int maxIterations = Integer.valueOf(line.getOptionValue("iterations",
            "10000"));
        Network network = new Network(thresholdError, outputFile,
            hiddenLayersNumber, hiddenLayerNeurons, learningConstant,
            maxIterations);
        network.learn();
      } else if ("race".equals(mode)) {
        requiredParameter(line, "scenario", options);

        String scenario = line.getOptionValue("scenario");
        Game game = new Game(scenario);
        game.run();
      }
    } catch (ParseException pe) {
      System.err.println("Command line parse exception: " + pe.getMessage());
    } catch (Exception e) {
      System.err.println("Fatal application exception: " + e.getMessage());
      e.printStackTrace();
    }

    System.exit(0);
  }
}
