package es.ucm.fdi.ici.c2021.practica3.grupo02.ghosts.actions;

import java.util.Random;

import es.ucm.fdi.ici.rules.Action;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

// Accion para perseguir al pacman directamente con un random ya que nunca le cogeras
public class DirectChase_A implements Action {
	
	GHOST me;
	
	public DirectChase_A(GHOST me) {
		super();
		this.me = me;
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = null;
		if (game.doesGhostRequireAction(this.me)) {       //if it requires an action
			if (game.getPacmanLastMoveMade() == game.getGhostLastMoveMade(this.me)) {
				int[] neigh = game.getNeighbouringNodes(game.getGhostCurrentNodeIndex(this.me), game.getGhostLastMoveMade(this.me));
				nextMove = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(this.me), neigh[new Random().nextInt(neigh.length)], game.getGhostLastMoveMade(this.me), DM.PATH);
			}
			else {
				nextMove = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(me), DM.PATH);
			}
        }
		return nextMove;
	}

	@Override
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}
}
