package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions;

import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsEdibleTransition implements Transition  {

	GHOST ghost;
	public GhostsEdibleTransition(GHOST ghost) {
		super();
		this.ghost = ghost;
	}



	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		switch(ghost) {
			case BLINKY:
				return input.isBLINKYedible();
			case INKY:
				return input.isINKYedible();
			case PINKY:
				return input.isPINKYedible();
			case SUE:
				return input.isSUEedible();
			default:
				return false;
		}
	}



	@Override
	public String toString() {
		return "Ghost is edible";
	}

	
	
}
