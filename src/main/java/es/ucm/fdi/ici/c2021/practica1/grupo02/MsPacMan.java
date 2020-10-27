package es.ucm.fdi.ici.c2021.practica1.grupo02;

import pacman.game.Game;
import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacMan extends PacmanController{
	private Game game;
	private final int GHOSTS_NUMBER = 4;
	private final int GHOSTS_VISIBILITY_LIMIT = 1000;
	private final int GHOSTS_VISIBILITY_FOLLOW_LIMIT = 100;
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		this.game=game;
		
		//si hay fantasmas comestibles y son el mas cercano los priorizamos porque son la fuente principal de puntos
		GHOST edibleGhost = getNearestEdibleGhost(GHOSTS_VISIBILITY_FOLLOW_LIMIT);
		if(edibleGhost != null && notExistAnyCloserGhost(edibleGhost)) {
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(edibleGhost), game.getPacmanLastMoveMade(), DM.PATH);
		}
		
		//si no huimos por las rutas sin fantasmas hacia pills o powerpills (si quedan)
		else {
			return runAway();
		}
	}
	
	
	private boolean notExistAnyCloserGhost(GHOST nearestGhost) {
		double distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), DM.PATH);
		for(GHOST g: GHOST.values()) {
			//si hay algun fantasma no comestible mas cercano que el comestible priorizo huir
			if(g!=nearestGhost && game.isGhostEdible(g)==false && game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), DM.PATH) < distance) {
				return false;
			}
		}
		return true;
	}
	
	
	private MOVE runAway() {
		MOVE nextMove = null;
		int pacmanNode = game.getPacmanCurrentNodeIndex();
		int pill=0, limit = 50;
		
		//si quedan powerpills y hay mas de dos fantasmas cerca priorizamos powerpill, en otro caso vamos a por pills
		if(game.getNumberOfActivePowerPills() > 0 && getNumGhosts(limit) >= 2) {
			pill = game.getClosestNodeIndexFromNodeIndex(pacmanNode, game.getActivePowerPillsIndices(), DM.PATH);
		}
		else {
			pill = game.getClosestNodeIndexFromNodeIndex(pacmanNode, game.getActivePillsIndices(), DM.PATH);
		}
		
		int[] neigh = game.getNeighbouringNodes(pacmanNode, game.getPacmanLastMoveMade());
		int[][] path = new int[3][];
		
		int i=0;
		boolean pathfound=false;
		for(int n:neigh) {
			//para cada nodo vecino quiero los caminos hacia powerpills que no tengan fantasmas en medio
			path[i] = game.getShortestPath(n, pill);
			if(this.pathContainsAnyGhost(path[i],GHOST.values())) {
				path[i]=null;
				break;
			}
			if (pathContainsPills(path[i],pill)){pathfound = true;} 
			i++;
		}
		//si hemos encontrado caminos hacia pills sin fantasmas
		if(pathfound) {
			 int n = 0; // Nodo con el cual se llega por el camino mas rapido
             int l = Integer.MAX_VALUE;
             for(int[] a : path){
                 if(a != null){
                     int d = a.length;
                     if(d < l) {
                         l = d;
                         n = a[0]; // Se pone el 1 para evitar que el [0] sea el propio nodo origen
                         nextMove = game.getNextMoveTowardsTarget(pacmanNode, n, DM.PATH);
                     }
                 }
             }
		}
		//si no huimos del mas cercano
		else {
			GHOST closest = getNearestChasingGhost(GHOSTS_VISIBILITY_LIMIT);
			if(closest!=null) {
				nextMove = game.getNextMoveAwayFromTarget(pacmanNode, game.getGhostCurrentNodeIndex(getNearestChasingGhost(GHOSTS_VISIBILITY_LIMIT)), DM.PATH);
			}
			
		}
		return nextMove;
	}
	
	private int getNumGhosts(int limit) {
		int i = 0;
		for(GHOST ghostType : GHOST.values()) {
			if(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.EUCLID) < limit) {
				i++;
			}
		}
		return i;
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
				if(game.getGhostCurrentNodeIndex(ghostType)==value /*&& game.getDistance(value,game.getPacmanCurrentNodeIndex(),DM.PATH) <= limit*/) {
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
	
	/*private int getNearestPill(int limit) {
		
		int ppill,pill;
		
		if(game.getNumberOfActivePowerPills() > 0) {
			ppill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),game.getActivePowerPillsIndices() , DM.PATH);
			//if(game.getDistance(game.getPacmanCurrentNodeIndex(), ppill , game.getPacmanLastMoveMade(), DM.PATH) <= limit) {
				return ppill;
			//}
		}
		pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.PATH);
		return pill;
	}*/
	
	private boolean pathContainsAnyGhost(int[] path, GHOST[] ghosts) {
		if (path != null && path.length > 0) {
			for (int nodeIndex : path) {
				for (GHOST g : ghosts) {
					if (game.getGhostCurrentNodeIndex(g) == nodeIndex) return true;
				}
			}
		}
		return false;
	}
	
	private boolean pathContainsPills(int[] path, int pill) {
		if (path != null && path.length > 0) {
			for (int nodeIndex : path) {
				if (pill == nodeIndex) return true;
			}
		}
		return false;
	}
	

}





