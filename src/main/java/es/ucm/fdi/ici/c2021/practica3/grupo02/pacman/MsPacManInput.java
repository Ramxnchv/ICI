package es.ucm.fdi.ici.c2021.practica3.grupo02.pacman;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import es.ucm.fdi.ici.rules.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;


public class MsPacManInput extends Input {
	
	public static final int GHOST_PROXIMITY_THRESHOLD = 80;
	public static final int LAIR_NODE = 1292;
	
	//mapas con info de fantasmas
	private Map<GHOST, Boolean> ghostEdible;
	private Map<GHOST, Double> ghostDistanceToPacman;
	
	//info de game para transiciones entre estados
	private GHOST nearestGhost;
	private int edibleGhosts;
	private boolean nearestGhostEdible;
	private int numberOfGhostsNear;
	private int activePowerPills;
	private double distance2Closest;
	private boolean freeGhostsPath;
	private int[] path;
	private boolean spawnPoint;
	
	//constructor
	public MsPacManInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		
		//Cargar los maps con ghosts para saber si son comestibles o no y sus distancias a pacman
		
		ghostEdible = new EnumMap<GHOST, Boolean>(GHOST.class);
		ghostDistanceToPacman = new EnumMap<GHOST, Double>(GHOST.class);
		
		for (GHOST g : GHOST.values()) {
			ghostEdible.put(g, game.isGhostEdible(g));
			ghostDistanceToPacman.put(g, game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), DM.EUCLID));
		}
		
		//Obtener fantasma mas cercano
		double min = Double.MAX_VALUE;
		for(Entry<GHOST, Double> e : ghostDistanceToPacman.entrySet()) {
			if (e.getValue() < min) {
				min = e.getValue();	
				nearestGhost = e.getKey();
			}
		}
		
		//Obtener distancia al mas cercano
		distance2Closest = 1000000000;
		if( game.getGhostCurrentNodeIndex(nearestGhost)!=LAIR_NODE && game.isGhostEdible(nearestGhost)==false) {
			this.distance2Closest = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), DM.PATH);
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
			boolean edible = game.isGhostEdible(g);
			boolean initialnode = game.getGhostLairTime(g) >= 0;
			
			if(edible==false && initialnode==false) {
				double distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), game.getPacmanLastMoveMade(), DM.PATH); 
				if(distance <= GHOST_PROXIMITY_THRESHOLD) {
					this.numberOfGhostsNear++;
				}
			}
		}
		
		//Comprobar powerpills activas
		this.activePowerPills = game.getNumberOfActivePowerPills();
		
		//Comprobar si existe algun camino hacia pill sin fantasmas
		int pacmanNode = game.getPacmanCurrentNodeIndex();
		int pill=0;
		if(game.getNumberOfActivePowerPills() > 0) {
			pill = game.getClosestNodeIndexFromNodeIndex(pacmanNode, game.getActivePowerPillsIndices(), DM.PATH);
		}
		else {
			pill = game.getClosestNodeIndexFromNodeIndex(pacmanNode, game.getActivePillsIndices(), DM.PATH);
		}
		
		int[] neigh = game.getNeighbouringNodes(pacmanNode, game.getPacmanLastMoveMade());
		
		boolean pathfound=false;
		
		for(int n:neigh) {
			//para cada nodo vecino quiero los caminos hacia pills que no tengan fantasmas en medio
			this.path = game.getShortestPath(n, pill);
			if(this.pathContainsAnyGhost(path,GHOST.values())==false) {
				pathfound = true; break;
			}
		}
		
		
		//devuelvo si existen caminos sin fantasmas para evaluate en transicion y el array de caminos
		this.freeGhostsPath = pathfound;
		
		//comprobar si pacman está en el punto de aparicion
		this.spawnPoint = game.getPacmanCurrentNodeIndex() == game.getPacManInitialNodeIndex();
	}
	
	//comprobacion sobre si el camino tiene fantasmas
	private boolean pathContainsAnyGhost(int[] path, GHOST[] ghosts) {
		if (path != null && path.length > 0) {
			for (int nodeIndex : path) {
				for (GHOST g : ghosts) {
					if (game.getGhostCurrentNodeIndex(g) == nodeIndex && game.isGhostEdible(g)==false) return true;
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

	public double getDistance2Closest() {
		return distance2Closest;
	}

	public boolean isSpawnPoint() {
		return spawnPoint || game.getNumberOfPills() == 0;
	}

	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(MSPACMAN (edibleGhosts %d) (nearestGhostEdible %s) (numberOfGhostsNear %d) (activePowerPills %d) (freeGhostsPath %s) (distance2Closest %d) (spawnPoint %s))", 
				getEdibleGhosts(), isNearestGhostEdible(), getNumberOfGhostsNear(), getActivePowerPills(), isFreeGhostsPath(), (int) getDistance2Closest(), isSpawnPoint()));
		
		return facts;
	}
	
}
