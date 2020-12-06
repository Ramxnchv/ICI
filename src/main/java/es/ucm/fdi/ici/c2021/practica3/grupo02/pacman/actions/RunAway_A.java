package es.ucm.fdi.ici.c2021.practica3.grupo02.pacman.actions;

import es.ucm.fdi.ici.rules.Action;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAway_A implements Action{
	
	//Obtengo fantasma mas cercano y huyo de el
	
	private int MsPacMan;

	
	int[] ghostPositions;
	
	@Override
	public MOVE execute(Game game) {
		this.MsPacMan = game.getPacmanCurrentNodeIndex();
		this.ghostPositions = new int[4];
		
		int i=0;
		for(GHOST g: GHOST.values()) {
			ghostPositions[i]=game.getGhostCurrentNodeIndex(g);
			i++;
		}
		
		int nearest = game.getClosestNodeIndexFromNodeIndex(MsPacMan, ghostPositions, DM.PATH);
		return game.getNextMoveAwayFromTarget(MsPacMan, nearest, DM.PATH);
		
	}

	@Override
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}
}
