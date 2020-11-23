package es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions;

import es.ucm.fdi.ici.fsm.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAway_A implements Action{
	private final int GHOSTS_VISIBILITY_LIMIT = 10000;
	private int MsPacMan;
	private Game game;
	private GHOST nearestGhost;
	
	@Override
	public MOVE execute(Game game) {
		this.game=game;
		this.MsPacMan = game.getPacmanCurrentNodeIndex();
		this.nearestGhost= getNearestChasingGhost();
		
		return game.getNextMoveAwayFromTarget(MsPacMan, game.getGhostCurrentNodeIndex(nearestGhost), DM.PATH);
		
	}
	
	private GHOST getNearestChasingGhost() {
		int [] ghostPositions = new int[4];
		
		int i=0;
		for (GHOST ghostType : GHOST.values()) {
			if(game.isGhostEdible(ghostType) == false) {
				ghostPositions[i]=game.getGhostCurrentNodeIndex(ghostType);
				i++;
			}
		}
		
		GHOST nearest=null;
		
		if(ghostPositions.length > 0) {
			int value = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), ghostPositions, DM.PATH);

			for (GHOST ghostType : GHOST.values()) {
				if(game.getGhostCurrentNodeIndex(ghostType)==value) {
					nearest = ghostType;
					break;
				}
			}
			
		}
		
		return nearest;
	}

}
