package es.ucm.fdi.ici.c2021.practica5.grupo02.pacman;


import java.util.Set;

import es.ucm.fdi.ici.c2021.practica5.grupo02.Input;
import es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.CBRengine.MsPacManDescription;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;

public class MsPacManInput implements Input {

	Integer dist2nearestEdibleGhost;
	
	Integer dist2nearestNotEdibleGhost; 
	
	Integer dist2nearestPP;
	
	Set<MOVE> posiblesDirs;
	
	MOVE pacmanLastMove;
	
	Integer score;
	
	Integer level;
	
	@Override
	public void parseInput(Game game) {
		computeNearestNotEdibleGhost(game);
		computeNearestEdibleGhost(game);
		computeNearestPPill(game);
		computePosibleDirs(game);
		pacmanLastMove = game.getPacmanLastMoveMade();
		score = game.getScore();
		level = game.getCurrentLevel();
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		description.setDist2nearestEdibleGhost(dist2nearestEdibleGhost);
		description.setDist2nearestNotEdibleGhost(dist2nearestNotEdibleGhost);
		description.setDist2nearestPP(dist2nearestPP);
		description.setPacmanLastMove(pacmanLastMove);
		description.setPosiblesDirs(posiblesDirs);
		description.setLevel(level);
		description.setScore(score);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private void computeNearestEdibleGhost(Game game) {
		dist2nearestEdibleGhost = Integer.MAX_VALUE;
		
		for(GHOST g: GHOST.values()) {
			if(game.isGhostEdible(g)) {
				int pos = game.getGhostCurrentNodeIndex(g);
				
				int distance; 
				if(pos != -1) 
					distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
				else
					distance = Integer.MAX_VALUE;
				
				if(distance < dist2nearestEdibleGhost)
				{
					dist2nearestEdibleGhost = distance;
				}
			}
		}
		
	}
	
	private void computeNearestNotEdibleGhost(Game game) {
		dist2nearestNotEdibleGhost = Integer.MAX_VALUE;
		
		for(GHOST g: GHOST.values()) {
			if(game.isGhostEdible(g) == false) {
				int pos = game.getGhostCurrentNodeIndex(g);
				
				int distance; 
				if(pos != -1) 
					distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
				else
					distance = Integer.MAX_VALUE;
				
				if(distance < dist2nearestNotEdibleGhost)
				{
					dist2nearestNotEdibleGhost = distance;
				}
			}
		}
	}
	
	private void computeNearestPPill(Game game) {
		dist2nearestPP = Integer.MAX_VALUE;
		for(int pos: game.getPowerPillIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < dist2nearestPP)
				dist2nearestPP = distance;
		}
	}
	
	private void computePosibleDirs (Game game) {
		int[] pacmanNeightbours = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		for (int n : pacmanNeightbours) {
			MOVE m = game.getMoveToMakeToReachDirectNeighbour(game.getPacmanCurrentNodeIndex(), n);
			this.posiblesDirs.add(m);
		}
	}
}
