package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.actions;

import es.ucm.fdi.ici.c2021.practica5.grupo02.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DownAction implements Action {
    
	public DownAction() {}
	
	@Override
	public MOVE execute(Game game) {
		return MOVE.DOWN;
    }

	@Override
	public String getActionId() {
		return "Down";
	}
}