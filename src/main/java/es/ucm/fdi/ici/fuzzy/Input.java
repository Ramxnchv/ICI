package es.ucm.fdi.ici.fuzzy;

import java.util.Map;

import pacman.game.Game;

/**
 * Interface to obtain the input variables required to evaluate the fuzzy over the game.
 * @author Juan Ant. Recio García - Universidad Complutense de Madrid
 */
public interface Input {
	
	/**
	 * Obtains the required variables from the game and stores them as attributes of the implementing subclasses.
	 */
	public abstract void parseInput(Game game);

	/**
	 * Returns a list of fuzzy values.
	 * The String key of the map corresponds to one variable that must be defined in the fcl file.
	 * @see es.ucm.fdi.ici.rules.RuleEngine
	 */
	public abstract Map<String,Double> getFuzzyValues(); 
}
