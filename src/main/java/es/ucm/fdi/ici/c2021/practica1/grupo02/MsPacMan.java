package es.ucm.fdi.ici.c2021.practica1.grupo02;

import pacman.game.Game;
import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacMan extends PacmanController{
	private Game game;
	private final int GHOSTS_NUMBER = 4;
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		this.game=game;
		int limit = 20;
		
		GHOST nearestGhost = getNearestChasingGhost(limit);
		if(nearestGhost != null) {
			return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), DM.PATH);
		}
		
		nearestGhost = getNearestEdibleGhost(limit);
		if(nearestGhost != null) {
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), DM.PATH);
		}
		
		int nearestPill = getNearestPill();
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getPillIndex(nearestPill), game.getPacmanLastMoveMade(), DM.PATH);
	}
	 
	private GHOST getNearestChasingGhost(int limit) {
		int [] ghostPositions = new int[GHOSTS_NUMBER];
		
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
				if(game.getGhostCurrentNodeIndex(ghostType)==value && game.getDistance(value,game.getPacmanCurrentNodeIndex(),DM.PATH) <= limit) {
					nearest = ghostType;
					break;
				}
			}
		}
		
		return nearest;
	}
	
	private GHOST getNearestEdibleGhost(int limit) {
		int [] ghostPositions = new int[GHOSTS_NUMBER];
		
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
	
	private int getNearestPill() {
		
		int ppill,pill;
		
		if(game.getNumberOfActivePowerPills() > 0) {
			
			ppill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),game.getActivePowerPillsIndices() , DM.PATH);
			pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.PATH);
			int min = (int) Math.min(game.getDistance(game.getPacmanCurrentNodeIndex(), ppill, game.getPacmanLastMoveMade(), DM.PATH), game.getDistance(game.getPacmanCurrentNodeIndex(), pill, game.getPacmanLastMoveMade(), DM.PATH));
			return min == game.getDistance(game.getPacmanCurrentNodeIndex(), ppill, game.getPacmanLastMoveMade(), DM.PATH) ? ppill : pill;
			
		} else {
			pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.PATH);
			return pill;
		}
		
	}
}
