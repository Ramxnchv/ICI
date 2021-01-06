package es.ucm.fdi.ici.c2021.practica4.grupo02.pacman.actions;

import es.ucm.fdi.ici.fuzzy.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseGhostAction implements Action {
    
private MOVE _nextTurn;
	
	public ChaseGhostAction(MOVE nextTurn) {
		_nextTurn = nextTurn;
	}

	@Override
	public MOVE execute(Game game) {
		return _nextTurn;
	}
            
}
