package es.ucm.fdi.ici.c2021.practica0.grupo02;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController{
	
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private Game game;
	private MOVE[] allMoves = MOVE.values();
	private Random rnd = new Random();
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		this.game=game;
		int limit = 15;
		moves.clear();
		
		for (GHOST ghostType : GHOST.values()) {
			if (game.doesGhostRequireAction(ghostType)) {
				if(game.isGhostEdible(ghostType) || pacmanCloseToPowerPill(limit)) {
					moves.put(ghostType, game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghostType), DM.PATH));
				}
				else if(rnd.nextFloat() < 0.9) {
					moves.put(ghostType,game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghostType), DM.PATH));
				}
				else {
					moves.put(ghostType, allMoves[rnd.nextInt(allMoves.length)]);
				}
			}
			else {
				return null;
			}
		}
		
		return moves;
	}
	
	private boolean pacmanCloseToPowerPill(int limit) {
		
		if(game.getNumberOfActivePowerPills()>0) {	
			int nearestPPill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),game.getActivePowerPillsIndices() , DM.PATH);
			return game.getDistance(nearestPPill,game.getPacmanCurrentNodeIndex(),DM.PATH) <= limit;
		}
		
		return false;
	}

}
