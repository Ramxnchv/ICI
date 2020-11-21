package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.GhostsInput;

// Pacman close to PPill & Ghost close to Pacman transition
public class PPill_GhostToPM_T implements Transition {

	private static double THRESHOLD = 30;
	private static double DANGER_RADIUS = 30;

	GHOST ghost;
	
	public PPill_GhostToPM_T(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;
		return input.getMinPacmanDistancePPill() < THRESHOLD && input.getGhostDistanceToPacman(ghost) < DANGER_RADIUS;
	}

	@Override
	public String toString() {
		return "MsPacman near PPill & Me to close to him";
	}
}
