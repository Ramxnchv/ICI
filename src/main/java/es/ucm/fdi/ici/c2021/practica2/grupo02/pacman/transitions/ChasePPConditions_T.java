package es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions;

import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.Transition;

public class ChasePPConditions_T implements Transition{

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return input.getNumberOfGhostsNear() > 2 && input.getActivePowerPills() > 0;
	}
	
	@Override
	public String toString() {
		return "2 ghosts near and ActivePowerPills > 0";
	}

}
