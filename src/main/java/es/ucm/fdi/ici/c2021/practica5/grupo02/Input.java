package es.ucm.fdi.ici.c2021.practica5.grupo02;


import pacman.game.Game;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;

/**
 * Interface to obtain the input variables required to evaluate the fuzzy over the game.
 * @author Juan Ant. Recio García - Universidad Complutense de Madrid
 */
public interface Input {
	
	/**
	 * Obtains the required variables from the game and stores them as attributes of the implementing subclasses.
	 */
	public abstract void parseInput(Game game);

	public abstract CBRQuery getQuery(); 
}
