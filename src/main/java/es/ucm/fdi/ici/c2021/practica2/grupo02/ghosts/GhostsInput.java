package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts;

import es.ucm.fdi.ici.fsm.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends Input {
	
	//---------------------------------------------------------------
	// GhostComestibles
	private boolean BLINKYedible;
	private boolean INKYedible;
	private boolean PINKYedible;
	private boolean SUEedible;
	
	// LairTime de los fantasmas
	private int BLINKYLairTime;
	private int INKYLairTime;
	private int PINKYLairTime;
	private int SUELairTime;
	private int[] GhostLairTime;
	
	// Tiempo que les queda para que NO sean comestibles
	private int[] GhostEdibleTime;
	
	// Distancia del Pacman a la Pill
	private double minPacmanDistancePPill;
	
	// Array con la distancia de los fantasmas al pacman
	private double []GhostDistanceToPacman;
	
	// Array con los diferentes fantasmas
	private GHOST []GhostTypes;
	
	// Fantasma mas cercano al pacman
	private GHOST closest;
	
	// ------------------------------ Constructor -------------------------------
	public GhostsInput(Game game) {
		super(game);
		// Creacion array distancias
		GhostDistanceToPacman = new double[GHOST.values().length];
		
		// Creacion array EdibleTime
		GhostEdibleTime = new int[GHOST.values().length];
		
		// Array LairTime
		GhostLairTime = new int[GHOST.values().length];
		
		// Creacion array con los tipos de fantasma
		GhostTypes = new GHOST[GHOST.values().length];
		GhostTypes[0] = GHOST.BLINKY;
		GhostTypes[1] = GHOST.INKY;
		GhostTypes[2] = GHOST.PINKY;
		GhostTypes[3] = GHOST.SUE;
	}

	@Override
	public void parseInput() {
		// Comestibles
		this.BLINKYedible = game.isGhostEdible(GHOST.BLINKY);
		this.INKYedible = game.isGhostEdible(GHOST.INKY);
		this.PINKYedible = game.isGhostEdible(GHOST.PINKY);
		this.SUEedible = game.isGhostEdible(GHOST.SUE);
	
		// Distancia del pacman a la pill mas cercana a el
		int pacmanNode = game.getPacmanCurrentNodeIndex();
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for(int ppill: game.getActivePowerPillsIndices()) {
			double distance = game.getDistance(pacmanNode, ppill, DM.PATH);
			this.minPacmanDistancePPill = Math.min(distance, this.minPacmanDistancePPill);
		}
		
		// Distancia de los fantasmas al pacman && EdibleTime && LairTime
		for(int i = 0; i < GHOST.values().length; i++) {
			// Distancia
			this.GhostDistanceToPacman[i] = game.getDistance(pacmanNode, game.getGhostCurrentNodeIndex(this.GhostTypes[i]), DM.PATH);
			// EdibleTime
			this.GhostEdibleTime[i] = game.getGhostEdibleTime(this.GhostTypes[i]);
			// LairTime
			this.GhostLairTime[i] = game.getGhostLairTime(this.GhostTypes[i]);
		}
		
		// Fantasma mas cercano
		closest = null;
		double minDist = 99999;
		for(int i = 0; i < GHOST.values().length - 1; i++) {
			minDist = Math.min(this.GhostDistanceToPacman[i], this.GhostDistanceToPacman[i+1]);
			if(minDist == this.GhostDistanceToPacman[i]) this.closest = this.GhostTypes[i];
			else { this.closest = this.GhostTypes[i+1]; }
		}
	}
	
	//------------------------------------- GETTERS --------------------------------------------
	// Fantasma mas cercano al pacman
	public GHOST getClosest() {
		return closest;
	}
	//--------------------------------------
	// Edible Time
	public double BLINKYEdibleTime() {
		return GhostEdibleTime[0];
	}
		
	public double INKYEdibleTime() {
		return GhostEdibleTime[1];
	}
		
	public double PINKYEdibleTime() {
		return GhostEdibleTime[2];
	}
		
	public double SUEEdibleTime() {
		return GhostEdibleTime[3];
	}
	//--------------------------------------
	// Lair Time
	public double BLINKYLairTime() {
		return GhostLairTime[0];
	}
		
	public double INKYLairTime() {
		return GhostLairTime[1];
	}
		
	public double PINKYLairTime() {
		return GhostLairTime[2];
	}
		
	public double SUELairTime() {
		return GhostLairTime[3];
	}
	//--------------------------------------
	// Distancias
	public double distToBLINKY() {
		return GhostDistanceToPacman[0];
	}
	
	public double distToINKY() {
		return GhostDistanceToPacman[1];
	}
	
	public double distToPINKY() {
		return GhostDistanceToPacman[2];
	}
	
	public double distToSUE() {
		return GhostDistanceToPacman[3];
	}
	//--------------------------------------
	// Comestibles
	public boolean isBLINKYedible() {
		return BLINKYedible;
	}

	public boolean isINKYedible() {
		return INKYedible;
	}

	public boolean isPINKYedible() {
		return PINKYedible;
	}

	public boolean isSUEedible() {
		return SUEedible;
	}
	//--------------------------------------
	// Distancia a la pill mas cercana
	public double getMinPacmanDistancePPill() {
		return minPacmanDistancePPill;
	}
}
