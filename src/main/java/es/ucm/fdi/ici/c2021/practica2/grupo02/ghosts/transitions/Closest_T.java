package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.GhostsInput;

// Ghost closest to the pacman transition
public class Closest_T implements Transition {
	
	GHOST ghost;
	
	public Closest_T(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;
		return input.getClosest() == ghost;
	}
	
	@Override
	public String toString() {
		return "Im the closest to the Pacman";
	}
}
