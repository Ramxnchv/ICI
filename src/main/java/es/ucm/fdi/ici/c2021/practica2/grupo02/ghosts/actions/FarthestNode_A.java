package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.actions;

import es.ucm.fdi.ici.fsm.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FarthestNode_A implements Action {

	GHOST me;
	
	public FarthestNode_A(GHOST me) {
		super();
		this.me = me;
	}
	
	@Override
	public MOVE execute(Game g) {
		// TODO Auto-generated method stub
		return null;
	}

}
