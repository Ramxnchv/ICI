package es.ucm.fdi.ici.c2021.practica1.grupo02;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Maze;
import pacman.game.internal.Node;

public class Ghosts extends GhostController{
	
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private Game game;
	
	private final int PACMAN_PROXIMITY_DISTANCE = 100;
	private final int GHOST_SAFETY_DISTANCE = 70;
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		int limit = 5;
		this.game=game;
        moves.clear();
        
        GHOST closestGhost = getClosestGhost();
        
        int farthestNodeFromPacman = game.getFarthestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), mazeToIndeces(game.getCurrentMaze()), DM.PATH);
		
		for (GHOST ghostType : GHOST.values()) {
			if (game.doesGhostRequireAction(ghostType)) {
                // El pacman esta cerca de una pill o ya la tiene activada
				if(game.isGhostEdible(ghostType) || pacmanCloseToPowerPill(limit)) {
                    // Separarse en funcion de los demas Ghosts
					moves.put(ghostType, runawayFromPacman(ghostType, closestGhost, farthestNodeFromPacman));
                }
                // Nos lo podemos comer
				else {
                    if(ghostType != closestGhost){
                        // Si hay un fantasma mas cerca del pacman que yo, busco otra ruta
                        moves.put(ghostType, findCutRoute(ghostType, closestGhost));
                    }
                    else { // Soy el mas cercano al pacman asik a por el wachin
                        moves.put(ghostType, goForPacman(ghostType));
                    }
				}
			}
        }
        System.out.println(moves);
		return moves;
    }
	
	private MOVE runawayFromPacman(GHOST me, GHOST closest, int farthest) {
		
		if (me == closest || game.getDistance(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex(), DM.EUCLID) > GHOST_SAFETY_DISTANCE) {
			return game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(me), DM.EUCLID);	
		}
		
		else {
            int[] neigh = game.getNeighbouringNodes(game.getGhostCurrentNodeIndex(me), game.getGhostLastMoveMade(me));
            
            int[] shortestPath = null;
            int shortestPathLength = Integer.MAX_VALUE; 
            for (int n : neigh) {
            	int[] path = game.getShortestPath(game.getGhostCurrentNodeIndex(me), farthest);
            	if (!pathContainsOtherGhost(path, me, GHOST.values()) && !this.pathContainsPacman(shortestPath, game.getPacmanCurrentNodeIndex()) && path.length <= shortestPathLength) {
            		shortestPath = path;
            		shortestPathLength = path.length;
            	}
            }
            
            if (shortestPath == null) return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(me), neigh[new Random().nextInt(neigh.length)], game.getGhostLastMoveMade(me), DM.PATH);
            else return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(me), shortestPath[0], game.getGhostLastMoveMade(me), DM.PATH);
		}
		
	}

    private GHOST getClosestGhost() {
        GHOST closest = null;
        double closestDist = Double.MAX_VALUE;
        for (GHOST g : GHOST.values()){
            double temp = game.getDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), DM.PATH);
            if(temp < closestDist) {
                closest = g;
                closestDist = temp;
            }
        }
        return closest;
    }

    private MOVE goForPacman(GHOST me) {
        return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(me), DM.PATH);
    }

    private MOVE findCutRoute(GHOST me, GHOST closest) {
        MOVE nextMove = null;

        int meNodeIndex = game.getGhostCurrentNodeIndex(me);
        int pacmanNodeIndex = game.getPacmanCurrentNodeIndex();

        double distToPacman = game.getDistance(meNodeIndex, pacmanNodeIndex, DM.PATH);

        // Si estas lo suficientemente cerca del pacMan para buscar una ruta alternativa
        if (distToPacman < PACMAN_PROXIMITY_DISTANCE) {

            MOVE meLastMove = game.getGhostLastMoveMade(me);
            GHOST other1 = null, other2 = null;

            // Encuentro de los otros fantasmas
            for(GHOST g : GHOST.values()){
                if(g != me && g != closest) {
                    other1 = g;
                }
                else if(g != other1) {
                    other2 = g;
                }
            }

            // Nodos vecinos al actual y array con los movimientos de los caminos
            int[] neigh = game.getNeighbouringNodes(meNodeIndex, meLastMove);
            int [][] movements = new int[3][];
            
            int i = 0;
            boolean Pathfound = false;

            // Elegimos aquellos caminos en los que no hay un fantasma
            for(int n : neigh) {
                movements[i] = game.getShortestPath(n, pacmanNodeIndex);
                for(int N : movements[i]) {
                    if (N == game.getGhostCurrentNodeIndex(closest) ||
                        N == game.getGhostCurrentNodeIndex(other1) ||
                        N == game.getGhostCurrentNodeIndex(other2)) {
                            break;
                        }
                    if(N == pacmanNodeIndex) 
                        Pathfound = true;
                        
                }
                i++;
            }

            // Si se ha encontrado caminos sin fantasmas se elige el mas rapido
            if (Pathfound) {
                int n = 0; // Nodo con el cual se llega por el camino mas rapido
                int l = Integer.MAX_VALUE;
                for(int[] a : movements){
                    if(a != null){
                        int d = a.length;
                        if(d < l) {
                            l = d;
                            n = a[1]; // Se pone el 1 para evitar que el [0] sea el propio nodo origen
                            nextMove = game.getNextMoveTowardsTarget(meNodeIndex, n, DM.PATH);
                        }
                    }
                }
            }
        } else {
            nextMove = goForPacman(me);
        }
        return nextMove;
    }

	private boolean pacmanCloseToPowerPill(int limit) {
		if(game.getNumberOfActivePowerPills()>0) {	
			int nearestPPill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),game.getActivePowerPillsIndices() , DM.PATH);
			return game.getDistance(nearestPPill,game.getPacmanCurrentNodeIndex(),DM.PATH) <= limit;
		}
		
		return false;
    }
	
	private int[] mazeToIndeces(Maze m) {
		int[] indices = new int[m.graph.length];	int i = 0;	
		for (Node n : m.graph) {	indices[i] = n.nodeIndex;	++i;	}
		return indices;
	}
	
	private boolean pathContainsPacman(int[] path, int pacmanIndex) {
		if (path != null && path.length > 0) {
			for (int nodeIndex : path) {
				if (pacmanIndex == nodeIndex) return true;
			}
		}
		return false;
	}

	private boolean pathContainsOtherGhost(int[] path, GHOST me, GHOST[] ghosts) {
		if (path != null && path.length > 0) {
			for (int nodeIndex : path) {
				for (GHOST g : ghosts) {
					if (g != me && game.getGhostCurrentNodeIndex(g) == nodeIndex) return true;
				}
			}
		}
		return false;
	}
}