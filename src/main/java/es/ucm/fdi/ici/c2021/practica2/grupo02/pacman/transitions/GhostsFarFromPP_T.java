package es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions;

import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;

public class GhostsFarFromPP_T implements Transition{

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return input.getDistance2Closest() >= MsPacManInput.GHOST_PROXIMITY_THRESHOLD;
	}
	
	@Override
	public String toString() {
		return "Ghosts Far from PowerPill";
	}

}
