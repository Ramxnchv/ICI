package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions;

import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class ShortEdibleTime_NotTooClose_T implements Transition {

	GHOST ghost;
	
	public ShortEdibleTime_NotTooClose_T(GHOST ghost) {
		super();
		this.ghost = ghost;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;
		return input.getGhostDistanceToPacman(ghost) > GhostsInput.PACMAN_DANGER_THRESHOLD && input.getEdibleTime(ghost) < GhostsInput.SHORT_EDIBLE_TIME_LIMIT;
	}

}
