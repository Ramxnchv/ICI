package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.GhostsInput;

// Pacman close to PPill & Ghost close to Pacman transition
public class PPill_GhostToPM_T implements Transition {

	public static double thresold = 30;
	public static double myDist = 30;

	GHOST ghost;
	
	public PPill_GhostToPM_T(GHOST ghost) {
		super();
		this.ghost = ghost;
	}


	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;
		return input.getMinPacmanDistancePPill() < thresold && myDistToPacman(input);
	}
	
	public boolean myDistToPacman(GhostsInput input) {
		switch(ghost) {
			case BLINKY:
				return input.distToBLINKY() < myDist;
			case INKY:
				return input.distToINKY() < myDist;
			case PINKY:
				return input.distToPINKY() < myDist;
			case SUE:
				return input.distToSUE() < myDist;
			default:
				return false;
		}
	}


	@Override
	public String toString() {
		return "MsPacman near PPill & Me to close to him";
	}
}
