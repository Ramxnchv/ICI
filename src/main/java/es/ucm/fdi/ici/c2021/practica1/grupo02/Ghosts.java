package es.ucm.fdi.ici.c2021.practica1.grupo02;

import java.util.EnumMap;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController{
	
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private Game game;
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		int limit = 5;
		this.game=game;
        moves.clear();
        
        GHOST closestGhost = getClosestGhost();
		
		for (GHOST ghostType : GHOST.values()) {
			if (game.doesGhostRequireAction(ghostType)) {
                // El pacman esta cerca de una pill o ya la tiene activada
				if(game.isGhostEdible(ghostType) || pacmanCloseToPowerPill(limit)) {
                    // Separarse en funcion de los demas Ghosts
					moves.put(ghostType, game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghostType), DM.PATH));
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
        /*
        int[] pacManPos = new int[2];
        pacManPos[0] = game.getNodeXCood(game.getPacmanCurrentNodeIndex());
        pacManPos[1] = game.getNodeYCood(game.getPacmanCurrentNodeIndex());
        MOVE pacmanDir = game.getPacmanLastMoveMade();

        int[] closestPos = new int[2];
        closestPos[0] = game.getNodeXCood(game.getGhostCurrentNodeIndex(closest));
        closestPos[1] = game.getNodeYCood(game.getGhostCurrentNodeIndex(closest));
        MOVE closestDir = game.getGhostLastMoveMade(closest);

        // Posicion del fantasma (no closest)
        int[] myPos = new int[2];
        myPos[0] = game.getNodeXCood(game.getGhostCurrentNodeIndex(me));
        myPos[1] = game.getNodeYCood(game.getGhostCurrentNodeIndex(me));

        // Distancia del mas cercano al pacman
        int xDist = Math.abs(pacManPos[0] - closestPos[0]);
        int yDist = Math.abs(pacManPos[1] - closestPos[1]);

        // Ver si estas en la misma fila/columna que el mas cercano
        int x = Math.abs(closestPos[0] - myPos[0]);
        int y = Math.abs(closestPos[1] - myPos[1]);

        

        // Si se le asigna la misma direccion que al que esta mas cerca
        // se encuentra en la misma fila/columna que el mas cercano
        // y esta a menos de 3 de distancia del mas cercano.
        if (goForPacman(me) == closestDir && (x == 0 || y == 0) &&
            game.getDistance(meNodeIndex, game.getGhostCurrentNodeIndex(closest), DM.PATH) < 20) {
            // Buscar camino alternativo
            MOVE posibols[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(me));
            double index[] = new double[posibols.length];
            int i = 0;
            for(MOVE m : posibols){
                if(m == closestDir){
                    index[i] = Double.MAX_VALUE;
                } else {
                    switch (m) {
                        case RIGHT: //+1
                            //game.getwa
                            game.getMoveToMakeToReachDirectNeighbour(currentNodeIndex, neighbourNodeIndex)
                            game.getNeighbouringNodes(meNodeIndex, lastModeMade)
                            index[i] = game.getDistance(meNodeIndex + 1, game.getGhostCurrentNodeIndex(closest), DM.PATH);
                        break;
                        case LEFT:  //-1
                            index[i] = game.getDistance(meNodeIndex - 1, game.getGhostCurrentNodeIndex(closest), DM.PATH);
                        break;
                        case UP:    //+27
                            index[i] = game.getDistance(meNodeIndex + 27, game.getGhostCurrentNodeIndex(closest), DM.PATH);
                        break;
                        case DOWN:  //-27
                            index[i] = game.getDistance(meNodeIndex - 27, game.getGhostCurrentNodeIndex(closest), DM.PATH);
                        break;
                        default:
                            break;
                    }
                }
                i++;
            }
            
            //nextMove = game.getMoveToMakeToReachDirectNeighbour(meNodeIndex, neighbourNodeIndex);

            i = 0;
            int movement = 0;
            double min = Double.MAX_VALUE;
            for(double d : index) {
                if(d < min){
                    min = d;
                    movement = i;
                }
                i++;
            }

            nextMove = posibols[movement];

        }else{
            nextMove = goForPacman(me);
        }
        */
        //-------------------------------------------------

        int meNodeIndex = game.getGhostCurrentNodeIndex(me);
        int pacmanNodeIndex = game.getPacmanCurrentNodeIndex();

        double distToPacman = game.getDistance(meNodeIndex, pacmanNodeIndex, DM.PATH);

        // Si estas lo suficientemente cerca del pacMan para buscar una ruta alternativa
        if (distToPacman < 100) {

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
}