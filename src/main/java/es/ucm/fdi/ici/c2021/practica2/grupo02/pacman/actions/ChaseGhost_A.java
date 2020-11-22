package es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions;

import es.ucm.fdi.ici.fsm.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseGhost_A implements Action {
	private final int GHOSTS_VISIBILITY_FOLLOW_LIMIT = 100;
	private int MsPacMan;
	private Game game;
	private GHOST chasedGhost;
	
	
	@Override
	public MOVE execute(Game game) {
		this.game= game;
		this.MsPacMan = game.getPacmanCurrentNodeIndex();
		this.chasedGhost = getNearestEdibleGhost(GHOSTS_VISIBILITY_FOLLOW_LIMIT);
		return game.getApproximateNextMoveTowardsTarget(MsPacMan, game.getGhostCurrentNodeIndex(chasedGhost), game.getPacmanLastMoveMade(), DM.PATH);
	}
	
	private GHOST getNearestEdibleGhost(int limit) {
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
			if(game.getGhostCurrentNodeIndex(ghostType)==value && game.getDistance(value,game.getPacmanCurrentNodeIndex(),DM.PATH) <= limit) {
				nearestEdible = ghostType;
				break;
			}
		}
		return nearestEdible;
	}

}