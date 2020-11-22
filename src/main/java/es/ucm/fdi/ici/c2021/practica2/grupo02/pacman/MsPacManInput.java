package es.ucm.fdi.ici.c2021.practica2.grupo02.pacman;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import es.ucm.fdi.ici.fsm.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class MsPacManInput extends Input {
	
	public static final int GHOST_PROXIMITY_THRESHOLD = 50;
	
	//mapas con info de fantasmas
	private Map<GHOST, Boolean> ghostEdible;
	private Map<GHOST, Double> ghostDistanceToPacman;
	
	//info de game para transiciones entre estados (simples) dentro de chase
	public GHOST nearestGhost;
	public int edibleGhosts;
	public boolean nearestGhostEdible;
	public int numberOfGhostsNear;
	public int activePowerPills;
	
	//info de game para transicion desde chase a runAway y viceversa
	public boolean freeGhostsPath;
	private int[][] path;
	
	//constructor
	public MsPacManInput(Game game) {
		super(game);
		ghostEdible = new HashMap<GHOST, Boolean>();
		ghostDistanceToPacman = new HashMap<GHOST, Double>();
		
	}

	@Override
	public void parseInput() {
		
		//Cargar los maps con ghosts para saber si son comestibles o no y sus distancias a pacman
		for (GHOST g : GHOST.values()) {
			ghostEdible.put(g, game.isGhostEdible(g));
			ghostDistanceToPacman.put(g, game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), DM.PATH));
		}
		
		//Obtener fantasma mas cercano
		double min = Double.MAX_VALUE;
		for(Entry<GHOST, Double> e : ghostDistanceToPacman.entrySet()) {
			if (e.getValue() < min) {
				min = e.getValue();	
				nearestGhost = e.getKey();
			}
		}
		
		//Obtener numero de fantasmas comestibles
		this.edibleGhosts = 0;
		
		for(Entry<GHOST, Boolean> e : ghostEdible.entrySet()) {
			if (e.getValue()) {
				this.edibleGhosts++;
			}
		}
		
		//Comprobar si el fantasma mas cercano es comestible
		this.nearestGhostEdible = game.isGhostEdible(nearestGhost);
		
		//Comprobar numero de fantasmas cercanos a PacMan
		this.numberOfGhostsNear = 0;
		for(GHOST g: GHOST.values()) {
			if(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), DM.PATH) <= GHOST_PROXIMITY_THRESHOLD) {
				this.numberOfGhostsNear++;
			}
		}
		
		//Comprobar powerpills activas
		this.activePowerPills = game.getNumberOfActivePowerPills();
		
		//Comprobar si existe algun camino hacia pill sin fantasmas
		int pacmanNode = game.getPacmanCurrentNodeIndex();
		int pill=0;
		pill = game.getClosestNodeIndexFromNodeIndex(pacmanNode, game.getActivePillsIndices(), DM.PATH);
		
		int[] neigh = game.getNeighbouringNodes(pacmanNode, game.getPacmanLastMoveMade());
		int[][] path = new int[3][];
		
		int i=0;
		boolean pathfound=false;
		for(int n:neigh) {
			//para cada nodo vecino quiero los caminos hacia pills que no tengan fantasmas en medio
			path[i] = game.getShortestPath(n, pill);
			if(this.pathContainsAnyGhost(path[i],GHOST.values())) {
				path[i]=null;
				break;
			}
			if (pathContainsPills(path[i],pill)){pathfound = true;} 
			i++;
		}
		//devuelvo si existen caminos sin fantasmas para evaluate en transicion y el array de caminos
		this.freeGhostsPath = pathfound;
		this.path = path;
		
	}
	
	//comprobacion sobre si el camino tiene fantasmas
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
	
	//comprobacion sobre si el camino tiene pills
	private boolean pathContainsPills(int[] path, int pill) {
		if (path != null && path.length > 0) {
			for (int nodeIndex : path) {
				if (pill == nodeIndex) return true;
			}
		}
		return false;
	}
	
	//GETTERS
	
	public GHOST getNearestGhost() {
		return nearestGhost;
	}

	public int getEdibleGhosts() {
		return edibleGhosts;
	}

	public boolean isNearestGhostEdible() {
		return nearestGhostEdible;
	}

	public int getNumberOfGhostsNear() {
		return numberOfGhostsNear;
	}

	public int getActivePowerPills() {
		return activePowerPills;
	}

	public boolean isFreeGhostsPath() {
		return freeGhostsPath;
	}

	public int[][] getPath() {
		return path;
	}
	
}
