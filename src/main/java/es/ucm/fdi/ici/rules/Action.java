package es.ucm.fdi.ici.rules;

import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * An action to be executed once asserted in the Rule Engine.
 * The asserted fact must comply the following syntax:
 * (ACTION (id action) [(slot ...) (slot ...) ...])
 * @author Juan Ant. Recio García - Universidad Complutense de Madrid
 */
public interface Action {
		
	public static final String FACT_NAME = "ACTION";
	public static final String ID_SLOT = "id";

	/**
	 * This method allows to parameterize the action from the slots in the corresponding fact.
	 * @param actionFact
	 */
	public void parseFact(Fact actionFact);
	
	
	/**
	 * Executes the action according to the game and returns the following move.
	 */
	public MOVE execute(Game game);
}
