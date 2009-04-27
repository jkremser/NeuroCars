package neurocars.neuralNetwork.service;

/**
 * Transformuje hodnotu napr. pomoci sigmoidalni fce
 * @author Martin
 *
 */
public class Transformer {
	
	public static double sigmoidal(double value){
	
		double eToValue = 1/java.lang.Math.exp(value);
		return 1/(eToValue + 1);
		//kdo vi, jak je to efektivni. 
		//Pokud by treba byla value normovana, byla by lepsi fce expm1
		//jenze to nikdy pravdepodobne kolem nuly vzdy nebude, takze nic
	}
	
	public static double tangens(double value){
		return 0;//OPTIONAL
	}

}
