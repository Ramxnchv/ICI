package es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions;

import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;

public class NotExistAnyFreeGhostsPath_T implements Transition{

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return input.isFreeGhostsPath() == false || input.getNumberOfGhostsNear() >= 3 || input.getDistance2Closest() <= 25;
	}

	@Override
	public String toString() {
		return "Todos los caminos tienen fantasmas";
	}
	
}
