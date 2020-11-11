package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.GhostsInput;
import pacman.game.Constants.GHOST;

public class GhostsNotEdibleAndPacManFarPPill_T implements Transition {

	GHOST ghost;
	public GhostsNotEdibleAndPacManFarPPill_T(GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		GhostEdible_T edible = new GhostEdible_T(ghost);
		PPill_GhostToPM_T near = new PPill_GhostToPM_T(ghost);
		return !edible.evaluate(input) && !near.evaluate(input);
	}

	@Override
	public String toString() {
		return "Ghost not edible and MsPacman far PPill";
	}
	
}
