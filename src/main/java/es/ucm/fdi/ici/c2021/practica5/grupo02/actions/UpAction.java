package es.ucm.fdi.ici.c2021.practica5.grupo02.actions;

import es.ucm.fdi.ici.c2021.practica5.grupo02.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class UpAction implements Action {
    
	public UpAction() {}
	
	@Override
	public MOVE execute(Game game) {
		return MOVE.UP;
    }

	@Override
	public String getActionId() {
		return "Up";
	}
}