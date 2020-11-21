package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions;

import pacman.game.Constants.GHOST;

import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.GhostsInput;

public class GhostEdible_T implements Transition  {

	GHOST ghost;
	public GhostEdible_T(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		return input.getGhostEdible(ghost);
	}

	@Override
	public String toString() {
		return "Ghost is edible";
	}
}



