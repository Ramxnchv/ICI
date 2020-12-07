package es.ucm.fdi.ici.c2021.practica3.grupo02.ghosts;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.ici.rules.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends Input {
	
	public static final int PACMAN_DANGER_THRESHOLD = 50;
	public static final int PPILL_PROXIMITY_THRESHOLD = 30;
	public static final int SHORT_EDIBLE_TIME_LIMIT = 30;
	
	private Map<GHOST, Boolean> ghostEdible;
	private Map<GHOST, Boolean> ghostEaten;
	private Map<GHOST, Integer> ghostLairTime;
	private Map<GHOST, Integer> ghostEdibleTime;
	private Map<GHOST, Double> ghostDistanceToPacman;
	
	// Distancia del Pacman a la Pill
	private double minPacmanDistancePPill;
	
	// Fantasma mas cercano al pacman
	private GHOST closestGhostToPacman;
	
	// ------------------------------ Constructor -------------------------------
	public GhostsInput(Game game) {
		super(game);		
	}

	@Override
	public void parseInput() {
		
		ghostEdible = new EnumMap<GHOST, Boolean>(GHOST.class);
		ghostEaten = new EnumMap<GHOST, Boolean>(GHOST.class);
		ghostLairTime = new EnumMap<GHOST, Integer>(GHOST.class);
		ghostEdibleTime= new EnumMap<GHOST, Integer>(GHOST.class);
		ghostDistanceToPacman = new EnumMap<GHOST, Double>(GHOST.class);
		
		for (GHOST g : GHOST.values()) {
			ghostEdible.put(g, game.isGhostEdible(g));
			ghostEaten.put(g, game.wasGhostEaten(g));
			ghostLairTime.put(g, game.getGhostLairTime(g));
			ghostEdibleTime.put(g, game.getGhostEdibleTime(g));
			ghostDistanceToPacman.put(g, game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), DM.PATH));
		}
		
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for(int ppill: game.getActivePowerPillsIndices()) {
			double distance = game.getDistance(game.getPacmanCurrentNodeIndex(), ppill, DM.PATH);
			this.minPacmanDistancePPill = Math.min(distance, this.minPacmanDistancePPill);
		}
		
		// Fantasma mas cercano
		closestGhostToPacman = null;
		double min = Double.MAX_VALUE;
		for(Entry<GHOST, Double> e : ghostDistanceToPacman.entrySet()) {
			if (e.getValue() < min) {
				min = e.getValue();	
				closestGhostToPacman = e.getKey();
			}
		}
		
	}
	
	//------------------------------------- GETTERS --------------------------------------------
	// Fantasma mas cercano al pacman

	public GHOST getClosest() {
		return closestGhostToPacman;
	}
	
	public int getEdibleTime(GHOST g) {
		return ghostEdibleTime.get(g);
	} 
	
	public int getLairTime(GHOST g) {
		return ghostLairTime.get(g);
	} 
	
	public boolean getGhostEdible(GHOST g) {
		return ghostEdible.get(g);
	}
	
	public boolean getGhostEaten(GHOST	g) {
		return ghostEaten.get(g);
	}
	
	public double getGhostDistanceToPacman(GHOST g) {
		return ghostDistanceToPacman.get(g);
	}
	
	public double getMinPacmanDistancePPill() {
		return minPacmanDistancePPill;
	}
	
	public String closestToPacman(GHOST g) {
		if (g.equals(getClosest())) return "true";
		else return "false";
	}
	
	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(BLINKY (edible %s)(lairTime %d)(edibleTime %d)(distToPacman %d)(closestToPacman %s))", getGhostEdible(GHOST.BLINKY), getLairTime(GHOST.BLINKY), getEdibleTime(GHOST.BLINKY), (int)getGhostDistanceToPacman(GHOST.BLINKY), closestToPacman(GHOST.BLINKY)));
		facts.add(String.format("(INKY (edible %s)(lairTime %d)(edibleTime %d)(distToPacman %d)(closestToPacman %s))", getGhostEdible(GHOST.INKY), getLairTime(GHOST.INKY), getEdibleTime(GHOST.INKY), (int)getGhostDistanceToPacman(GHOST.INKY), closestToPacman(GHOST.INKY)));
		facts.add(String.format("(PINKY (edible %s)(lairTime %d)(edibleTime %d)(distToPacman %d)(closestToPacman %s))", getGhostEdible(GHOST.PINKY), getLairTime(GHOST.PINKY), getEdibleTime(GHOST.PINKY), (int)getGhostDistanceToPacman(GHOST.PINKY), closestToPacman(GHOST.PINKY)));
		facts.add(String.format("(SUE (edible %s)(lairTime %d)(edibleTime %d)(distToPacman %d)(closestToPacman %s))", getGhostEdible(GHOST.SUE), getLairTime(GHOST.SUE), getEdibleTime(GHOST.SUE), (int)getGhostDistanceToPacman(GHOST.SUE), closestToPacman(GHOST.SUE)));
		facts.add(String.format("(MSPACMAN (mindistancePPill %d))", 
		(int)getMinPacmanDistancePPill()));
		return facts;
	}
	
}
