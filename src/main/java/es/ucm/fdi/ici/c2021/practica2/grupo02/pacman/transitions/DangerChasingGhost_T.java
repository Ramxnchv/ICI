package es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions;

import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;

public class DangerChasingGhost_T implements Transition {

	@Override
	public boolean evaluate(Input in) {
		// TODO Auto-generated method stub
		MsPacManInput input = (MsPacManInput) in;
		return input.getNumberOfGhostsNear() >= 3;
	}
	
	@Override
	public String toString() {
		return "DANGER chasing ghost";
	}
	
}
