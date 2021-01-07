package es.ucm.fdi.ici.c2021.practica4.grupo02.pacman;

import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.fuzzy.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput implements Input {

	private double[] distance = {50,50,50,50};
	private double[] confidence = {100,100,100,100};
	private EnumMap <GHOST, EnumMap<MOVE, Integer>> movements;
	
	public MsPacManInput() {
		movements = new EnumMap<GHOST, EnumMap<MOVE, Integer>>(GHOST.class);
		for (GHOST g : GHOST.values()) movements.put(g, new EnumMap<MOVE, Integer>(MOVE.class));
	}
	
	@Override
	public void parseInput(Game game) {
		for(GHOST g: GHOST.values()) {
			int index = g.ordinal();
			int pos = game.getGhostCurrentNodeIndex(g);
			
			// vemos al ghost
			if(pos != -1) {
				try {
				// almaceno la distancia
				distance[index] = game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
				
				// pongo la confianza al maximo
				confidence[index] = 100;
				
				//almaceno la dirección de ese ghost
				MOVE ghostDirection = game.getGhostLastMoveMade(g);
				movements.get(g).put(ghostDirection, 100);
				movements.get(g).put(ghostDirection.opposite(), movements.get(g).get(ghostDirection.opposite())/2);
				
				//almaceno la dirección del fantasma respecto del pacman
				MOVE relativeDirection = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), pos, DM.EUCLID);
				movements.get(g).put(relativeDirection, 100);
				movements.get(g).put(relativeDirection.opposite(), movements.get(g).get(relativeDirection.opposite())/2);
				
				
				}catch(Exception e) { //no vemos al ghost
					if(confidence[index] > 1) confidence[index] -= 1;
					for (MOVE m : MOVE.values()) movements.get(g).put(m, Math.max(1, movements.get(g).get(m) - 1));
				}
				
			
			} else {
				// estamos en la posicion inicial asi que perdemos confianza mas rapidamente y reiniciamos conocimiento posiciones ghosts
				confidence[index] = Math.max(1, confidence[index] - .5);
				for (MOVE m : MOVE.values()) movements.get(g).put(m, 0);
			}
		}
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

	@Override
	public HashMap<String, Double> getFuzzyValues() {
		HashMap<String,Double> vars = new HashMap<String,Double>();
		for(GHOST g: GHOST.values()) {
			vars.put(g.name()+"distance",   distance[g.ordinal()]);
			vars.put(g.name()+"confidence", confidence[g.ordinal()]);
			vars.put(g.name()+"direction", parseDirection(g));
		}
		return vars;
	}

}
