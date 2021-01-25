package es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.actions;

import pacman.game.Game;
import es.ucm.fdi.ici.c2021.practica5.grupo02.Action;
import pacman.game.Constants.MOVE;

public class RightAction implements Action {
public RightAction() {	}
	
	@Override
	public MOVE execute(Game game) {
		return MOVE.RIGHT;
    }

	@Override
	public String getActionId() {
		return "Right";
	}
}
