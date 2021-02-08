package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import es.ucm.fdi.ici.c2021.practica5.grupo02.Action;

import pacman.game.Game;

public class GhostActionSelector {
	
	HashMap<String,Action> map;
	List<Action> actions;
	Game game;
	
	public GhostActionSelector(List<Action> actions) {
		this.map = new HashMap<String,Action>();
		for(Action a: actions)
			map.put(a.getActionId(), a);
		this.actions = actions;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	/**
	 * Method called when the CBREngine is not able to find a suitable action. 
	 * Simplest implementation returns a random one.
	 * @return
	 */
	// Se podria mejorar para intentar buscar la accion mejor en funcion del estado del juego y asi generar una base de casos fuerte más rapidamente
	public Action findAction() {
		int randomIndex = new Random().nextInt(actions.size());
		return actions.get(randomIndex);
	}
	
	/**
	 * Method called when the CBREngine has found a failed action.
	 * Simplest implementation returns another randomly.
	 * @return
	 */	
	// Las acciones están ordenadas en orden circular, por lo que desplazar en una unidad supone un giro de 90º y en 2 unidades de 180º
	public Action findAnotherAction(String failledAction) {
		int twist = ((new Random().nextInt() % 2 == 0) ? 2 : ((new Random().nextInt() % 2 == 0) ? -1 : 1));
		return actions.get((actions.indexOf(getAction(failledAction)) + twist) % actions.size());
	}

	public Action getAction(String action) {
		return map.get(action);
	}
}
