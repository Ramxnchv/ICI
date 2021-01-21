package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost;


import java.util.HashMap;

import es.ucm.fdi.ici.c2021.practica5.grupo02.Input;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine.GhostDescription;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;

public class GhostInput implements Input {
	// Almacenar la posicion relativa de los demas ghost respecto a ti (para poder sacar la ruta mas optima)
	HashMap <String, Double> otherGhostInfo;
	Integer nearestPPill;
	Boolean edible;
	Double iniDistToPacman;
	Integer level; // Nivel
	
	// Movimiento realizado por esta accion
	Integer movement; // 0, 1, 2, 3, 4 (up, down, left, right, neutral)
	Double finalDistToPacman; // *score*
	
	@Override
	public void parseInput(Game game) {
		for (GHOST g : GHOST.values()) {
			computeOtherGhost(game, g);
			computeNearestPPill(game, g);
			edible = game.isGhostEdible(g);
			iniDistToPacman = game.getDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), DM.EUCLID);
			level = game.getCurrentLevel();
		}
	}

	@Override
	public CBRQuery getQuery() {
		GhostDescription description = new GhostDescription();
		description.setEdible(edible);
		description.setNearestPPill(nearestPPill);
		description.setIniDistToPacman(iniDistToPacman);
		description.setLevel(level);
		
		//---PostAction---
		// No se si se ponen aqui o en otro lado jeje
		description.setFinalDistToPacman(finalDistToPacman);
		description.setMovement(movement);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private void computeOtherGhost(Game game, GHOST ghost) {
		otherGhostInfo = new HashMap<String,Double>();
		//GHOST nearest = null;
		for(GHOST g: GHOST.values()) {
			if(g != ghost) {
				//vars.put(g.name()+"distance", distance[g.ordinal()]);
				int pos = game.getGhostCurrentNodeIndex(g);
				int distance; 
				if(pos != -1) 
					distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
				else
					distance = Integer.MAX_VALUE;
				if(distance < nearestGhost)
				{
					nearestGhost = distance;
					nearest = g;
				}
			}
		}
		if(nearest!=null)
			edible = game.isGhostEdible(nearest);
	}
	
	private double parseDirection(GHOST g) {
		// (north + south + east + west)
		int[] direction_vector = {movements.get(g).get(MOVE.RIGHT)-movements.get(g).get(MOVE.LEFT), movements.get(g).get(MOVE.UP)-movements.get(g).get(MOVE.DOWN)};
		
		if (direction_vector[0] == 0 && direction_vector[1] == 0) return -1;
		
		// Calculamos el angulo que forma la suma de todos los vectores dirección con el eje Y (vector [0,1])
		
		double cos_a = (/*direction_vector[0] * 0 +*/ direction_vector[1] * 1) / (Math.sqrt(Math.pow(direction_vector[0], 2) + Math.pow(direction_vector[1], 2)) /** 1*/);  // (a * b) / mod(a) * mod(b)

		double angle = Math.acos(cos_a);
		double dir = (100*angle)/ (2*Math.PI);
		
		if (direction_vector[0] < 0) dir = 100 - dir;
		
		return dir;
	}
	
	private void computeNearestPPill(Game game, GHOST g) {
		nearestPPill = Integer.MAX_VALUE;
		for(int pos: game.getPowerPillIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < nearestPPill)
				nearestPPill = distance;
		}
	}
}
