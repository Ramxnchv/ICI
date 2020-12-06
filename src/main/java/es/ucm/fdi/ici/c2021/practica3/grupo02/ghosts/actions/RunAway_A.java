package es.ucm.fdi.ici.c2021.practica3.grupo02.ghosts.actions;

import es.ucm.fdi.ici.rules.Action;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAway_A implements Action {

    GHOST ghost;
    
	public RunAway_A(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost)) {        //if it requires an action
                return game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost),
                        game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
        }
        return MOVE.NEUTRAL;	
	}

	@Override
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}
}
