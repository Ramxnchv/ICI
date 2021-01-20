package es.ucm.fdi.ici.c2021.practica5.grupo02;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * An action to be executed as the solution of a case
 * @author Juan Ant. Recio García - Universidad Complutense de Madrid
 */
public interface Action {
	
	/**
	 * Returns the ID of the action
	 * @return
	 */
	public String getActionId();
	
	/**
	 * Executes the action according to the game and returns the following move.
	 */
	public MOVE execute(Game game);
}
