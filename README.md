# NeuroCars
This is a university project where we (team of 3 people) implemented the back-propagation neural network and train it to drive a car in a very simple environment. The learning data were replay files that were recorded when human player interacted with the environment.

### Structure

* replays: config/replay
* serialized nets: config/neuralnetwork
* maps: config/tracks
* scenarios: config/scenario


## Usage

compile:
```
mvn clean package
```

run the race, where there are 5 cars driven by the neural networks:
```bash
java -jar target/neurocars-0.0.1-SNAPSHOT-jar-with-dependencies.jar -scenario config/scenario/scenario-nn_training.properties -mode race
```

## Demo

![nn race demo](https://github.com/Jiri-Kremser/NeuroCars/raw/master/race.gif)
