package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions;

import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class NotTooCloseToPacman_T implements Transition {

	GHOST ghost;
	
	public NotTooCloseToPacman_T(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;
		return input.getGhostDistanceToPacman(ghost) > GhostsInput.PACMAN_DANGER_THRESHOLD;
	}

	@Override
	public String toString() {
		return "Ghost not too close to Pacman";
	}
}
