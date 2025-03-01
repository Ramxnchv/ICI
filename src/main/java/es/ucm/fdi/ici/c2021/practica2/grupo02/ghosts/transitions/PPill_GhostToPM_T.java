package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.GhostsInput;

// Pacman close to PPill & Ghost close to Pacman transition
public class PPill_GhostToPM_T implements Transition {

	GHOST ghost;
	
	public PPill_GhostToPM_T(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;
		return input.getMinPacmanDistancePPill() < GhostsInput.PPILL_PROXIMITY_THRESHOLD && input.getGhostDistanceToPacman(ghost) < GhostsInput.PACMAN_DANGER_THRESHOLD;
	}

	@Override
	public String toString() {
		return "MsPacman near PPill & Me to close to him";
	}
}
