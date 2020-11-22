package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostLeftBox_T implements Transition {

	GHOST ghost;

	public GhostLeftBox_T(GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input arg0) {
		return true;
	}

}
