package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.actions;

import es.ucm.fdi.ici.fsm.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

// Action para buscar caminos que corten la ruta del Pacman
public class FindCutRoute_A implements Action {

    GHOST ghost;
	public FindCutRoute_A( GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
        	MOVE nextMove = null;

            int meNodeIndex = game.getGhostCurrentNodeIndex(this.ghost);
            int pacmanNodeIndex = game.getPacmanCurrentNodeIndex();

            MOVE meLastMove = game.getGhostLastMoveMade(this.ghost);

            // Nodos vecinos al actual y array con los movimientos de los caminos
            int[] neigh = game.getNeighbouringNodes(meNodeIndex, meLastMove);
            int [][] path = new int[3][];
                
            int i = 0;
            boolean Pathfound = false;

            // Elegimos aquellos caminos en los que no hay un fantasma
            for(int n : neigh) {
                path[i] = game.getShortestPath(n, pacmanNodeIndex);
                if (pathContainsOtherGhost(path[i], this.ghost, GHOST.values(), game)) {
                    path[i] = null;
                    break;
                } 
                if (pathContainsPacman(path[i], game.getPacmanCurrentNodeIndex())) Pathfound = true;
                            
                i++;
            } // for neights

            // Si se ha encontrado caminos sin fantasmas se elige el mas rapido
            if (Pathfound) {
                int l = Integer.MAX_VALUE;
                for(int[] a : path){
                    if(a != null){
                        int d = a.length;
                        if(d < l) {
                            l = d;
                            nextMove = game.getNextMoveTowardsTarget(meNodeIndex, a[1], DM.PATH);
                        }
                    }
                } // for nPaths
            } // if pathFound
            return nextMove;
        } // if ghostRequireAction
        return MOVE.NEUTRAL;
	}


	private boolean pathContainsPacman(int[] path, int pacmanIndex) {
		if (path != null && path.length > 0) {
			for (int nodeIndex : path) {
				if (pacmanIndex == nodeIndex) return true;
			}
		}
		return false;
	}
	
	private boolean pathContainsOtherGhost(int[] path, GHOST me, GHOST[] ghosts, Game game) {
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