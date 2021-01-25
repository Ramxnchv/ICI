package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost;


import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2021.practica5.grupo02.Input;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine.GhostDescription;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;

public class GhostInput implements Input {
	GHOST me;
	Integer iniNodeIndex;
	// Info del fantasma mas cercano al ghost
	//Double closestGhostDist;
	//MOVE closestGhostRelPos;
	// Otros param de juego
	Integer nearestPPill;
	Double pacmanIniDist;
	MOVE pacmanRelPos;
	// Importantes a la hora de descartar
	Boolean edible;
	Integer level; // Nivel
	
	@Override
	public void parseInput(Game game) {
		//computeOtherGhost(game, me);
		computePacman(game, me);
		computeNearestPPill(game, me);
		iniNodeIndex = game.getGhostCurrentNodeIndex(me);
		edible = game.isGhostEdible(me);
		level = game.getCurrentLevel();
	}
	
	public void setGhost(GHOST g) {
		me = g;
	}

	@Override
	public CBRQuery getQuery() {
		GhostDescription description = new GhostDescription();
		description.setMe(me);
		description.setIniNodeIndex(iniNodeIndex);
		description.setEdible(edible);
		description.setLevel(level);
		description.setNearestPPill(nearestPPill);
		description.setPacmanIniDist(pacmanIniDist);
		description.setPacmanRelPos(pacmanRelPos);
		//description.setClosestGhostDist(closestGhostDist);
		//description.setClosestRelPos(closestGhostRelPos);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	// Info PACMAN
	private void computePacman(Game game, GHOST me) {
		pacmanIniDist = game.getDistance(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex(), DM.EUCLID);
		pacmanRelPos = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex(), DM.EUCLID);
	}

	// Info CLOSESTGHOST
	private void computeOtherGhost(Game game, GHOST me) {
		/*closestGhost = null;
		int pos = game.getGhostCurrentNodeIndex(me);
		for(GHOST g: GHOST.values()) {
			if(g != me) {
				Double distance; 
				if(pos != -1) 
					distance = game.getDistance(game.getGhostCurrentNodeIndex(g), pos, DM.PATH);
				else
					distance = Double.MAX_VALUE;
				if(distance < closestGhostDist)
				{
					closestGhost = g;
					closestGhostDist = distance;
				}
			}
		}
		
		if(closestGhost != null) {
			if(closestGhost != me) {
				closestGhostRelPos = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(me), game.getGhostCurrentNodeIndex(closestGhost), DM.EUCLID);
			}
			else {
				closestGhostDist = 0.0;
				closestGhostRelPos = MOVE.NEUTRAL;
			}
		}*/
	}
	
	// Info PPILLS
	private void computeNearestPPill(Game game, GHOST g) {
		nearestPPill = Integer.MAX_VALUE;
		for(int pos: game.getPowerPillIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < nearestPPill)
				nearestPPill = distance;
		}
	}
}
