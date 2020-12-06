package es.ucm.fdi.ici.c2021.practica3.grupo02.ghosts.actions;

import java.util.Random;

import es.ucm.fdi.ici.rules.Action;
import jess.Fact;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RandomMove_A implements Action {

	GHOST me;
	
	public RandomMove_A(GHOST me) {
		super();
		this.me = me;
	}

	@Override
	public MOVE execute(Game g) {
		if (g.doesGhostRequireAction(me)) {
			MOVE [] possibleMoves = g.getPossibleMoves(g.getGhostCurrentNodeIndex(me));
			return possibleMoves[(new Random()).nextInt() % possibleMoves.length];
		}
		
		return null;
	}

	@Override
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}
}