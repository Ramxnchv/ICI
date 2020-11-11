package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts;

import es.ucm.fdi.ici.fsm.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends Input{
	private boolean BLINKYedible;
	private boolean INKYedible;
	private boolean PINKYedible;
	private boolean SUEedible;
	private double minPacmanDistancePPill;
	
	private double PacmanDistanceToBLINKY;
	private double PacmanDistanceToINKY;
	private double PacmanDistanceToPINKY;
	private double PacmanDistanceToSUE;
	
	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		this.BLINKYedible = game.isGhostEdible(GHOST.BLINKY);
		this.INKYedible = game.isGhostEdible(GHOST.INKY);
		this.PINKYedible = game.isGhostEdible(GHOST.PINKY);
		this.SUEedible = game.isGhostEdible(GHOST.SUE);
	
		int pacman = game.getPacmanCurrentNodeIndex();
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for(int ppill: game.getActivePowerPillsIndices()) {
			double distance = game.getDistance(pacman, ppill, DM.PATH);
			this.minPacmanDistancePPill = Math.min(distance, this.minPacmanDistancePPill);
		}
		
		this.PacmanDistanceToBLINKY = game.getDistance(pacman, game.getGhostCurrentNodeIndex(GHOST.BLINKY), DM.PATH);
		this.PacmanDistanceToINKY = game.getDistance(pacman, game.getGhostCurrentNodeIndex(GHOST.INKY), DM.PATH);
		this.PacmanDistanceToPINKY = game.getDistance(pacman, game.getGhostCurrentNodeIndex(GHOST.PINKY), DM.PATH);
		this.PacmanDistanceToSUE = game.getDistance(pacman, game.getGhostCurrentNodeIndex(GHOST.SUE), DM.PATH);
		
	}
	
	// Distancias
	public double distToBLINKY() {
		return PacmanDistanceToBLINKY;
	}
	
	public double distToINKY() {
		return PacmanDistanceToINKY;
	}
	
	public double distToPINKY() {
		return PacmanDistanceToPINKY;
	}
	
	public double distToSUE() {
		return PacmanDistanceToSUE;
	}

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

	public double getMinPacmanDistancePPill() {
		return minPacmanDistancePPill;
	}
}
