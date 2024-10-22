package es.ucm.fdi.ici.c2021.practica3.grupo02.pacman.actions;

import es.ucm.fdi.ici.rules.Action;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChasePP_A implements Action{

	private int MsPacMan;
	private int powerPill;
	private Game game;
	
	@Override
	public MOVE execute(Game game) {
		
		//La accion de chasePowerPill consiste en seleccionar el camino mas corto sin fantasmas hacia la PowerPill mas cercana
		
		MOVE nextMove = MOVE.NEUTRAL;
		this.game=game;
		this.MsPacMan = game.getPacmanCurrentNodeIndex();
		this.powerPill = game.getClosestNodeIndexFromNodeIndex(MsPacMan, game.getActivePowerPillsIndices(), DM.PATH);
		
		int[] neigh = game.getNeighbouringNodes(MsPacMan, game.getPacmanLastMoveMade());
		
		int[][] path = getFreeGhostsPaths(neigh);
		
		
		 int n = 0; // Nodo con el cual se llega por el camino mas rapido
         int l = Integer.MAX_VALUE;
         for(int[] a : path){
             if(a != null && a.length>0){
                  int d = a.length;
                  if(d < l) {
                     l = d;
                     n = a[0]; // Se pone el 1 para evitar que el [0] sea el propio nodo origen
                     nextMove = game.getNextMoveTowardsTarget(MsPacMan, n, DM.PATH);
                  }
              }
         }
		
		return nextMove;
	}
	
	private int[][] getFreeGhostsPaths(int[] neigh){
		
		int[][] path = new int[3][];
		
		int i=0;
		for(int n:neigh) {
			//para cada nodo vecino quiero los caminos hacia powerpills que no tengan fantasmas en medio
			path[i] = game.getShortestPath(n, this.powerPill);
			if(this.pathContainsAnyGhost(path[i],GHOST.values())) {
				path[i]=null;
				break;
			}
			i++;
		}
		
		return path;
	}
	
	
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

	@Override
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}
}
