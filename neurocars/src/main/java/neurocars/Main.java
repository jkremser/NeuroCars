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
    help.printHelp("neurocars", options);
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
        File outputFile = new File(line.getOptionValue("output"));
        // TODO: konstanty .. nacist z parametru
        double tresholdError = 0.5;
        int hiddenLayersNumber = 1;
        int hiddenLayerSize = 10;
        double learningConstant = 0.2;
        int maxIterations = 100000;
        Network network = new Network(tresholdError, outputFile,
            hiddenLayersNumber, hiddenLayerSize, learningConstant,
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
