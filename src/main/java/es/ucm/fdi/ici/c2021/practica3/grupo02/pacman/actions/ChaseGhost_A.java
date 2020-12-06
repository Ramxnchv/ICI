package es.ucm.fdi.ici.c2021.practica3.grupo02.pacman.actions;

import es.ucm.fdi.ici.rules.Action;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseGhost_A implements Action {
	private int MsPacMan;
	private Game game;
	private GHOST chasedGhost;
	
	//La accion chaseGhost consiste en perseguir al fantasma comestible mas cercano
	
	@Override
	public MOVE execute(Game game) {
		this.game= game;
		this.MsPacMan = game.getPacmanCurrentNodeIndex();
		this.chasedGhost = getNearestEdibleGhost();
		if(chasedGhost!=null) {
			return game.getApproximateNextMoveTowardsTarget(MsPacMan, game.getGhostCurrentNodeIndex(chasedGhost), game.getPacmanLastMoveMade(), DM.PATH);
		}
		return MOVE.NEUTRAL;
	}
	
	private GHOST getNearestEdibleGhost() {
		int [] ghostPositions = new int[4];
		
		int i=0;
		for (GHOST ghostType : GHOST.values()) {
			if(game.isGhostEdible(ghostType)) {
				ghostPositions[i]=game.getGhostCurrentNodeIndex(ghostType);
				i++;
			}
		}
		
		int value = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), ghostPositions, DM.PATH);
		
		GHOST nearestEdible=null;
		
		for (GHOST ghostType : GHOST.values()) {
			if(game.getGhostCurrentNodeIndex(ghostType)==value) {
				nearestEdible = ghostType;
				break;
			}
		}
		return nearestEdible;
	}

	@Override
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}

}
