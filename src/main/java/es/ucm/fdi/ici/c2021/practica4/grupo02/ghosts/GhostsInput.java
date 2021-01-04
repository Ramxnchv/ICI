package es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts;

import java.util.HashMap;
import java.util.EnumMap;

import es.ucm.fdi.ici.fuzzy.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

public class GhostsInput implements Input {

	double[] distance = {50,50,50,50};
	double[] confidence = {100,100,100,100};
	double[] direction = {0, 0, 0, 0};
	
	EnumMap <MOVE, EnumMap<GHOST, Integer>> movements;

	@Override
	public void parseInput(Game game) {
		if(movements == null) {
			movements = new EnumMap<MOVE, EnumMap<GHOST, Integer>>(MOVE.class);
			for (MOVE m : MOVE.values()) {
				movements.put(m, new EnumMap<GHOST, Integer>(GHOST.class));
			}
		}
		
		
		for(GHOST g : GHOST.values()) {
			int index = g.ordinal();
			int pos = game.getGhostCurrentNodeIndex(g);
			if(pos != -1) {
				try {
					distance[index] = game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
					confidence[index] = 100;
					
					MOVE lastPacmanMove = game.getPacmanLastMoveMade();
					movements.get(lastPacmanMove).put(g, 50);
					
					movements.get(lastPacmanMove.opposite()).put(g, 0);
						
					MOVE extra = game.getApproximateNextMoveTowardsTarget(pos, game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g), DM.PATH);
					movements.get(extra).put(g, 50);
					
					movements.get(extra.opposite()).put(g, 0);
				} catch (Exception e) {
					if(confidence[index] > 1) confidence[index] -= 1;
					for (MOVE m : MOVE.values()) {
						if(movements.get(m).get(g) > 1) movements.get(m).put(g, movements.get(m).get(g) - 1);
					}
				}
			} else if (confidence[index] > 0) {
				confidence[index]-=.1;
			}

			if (game.isGhostEdible(g)) {
				confidence[index] = 0;
			} else {
				double ud = 0;
				double lr = 0;
				for (MOVE m : MOVE.values()) {
					if(movements.get(m).get(g) != 0) {
						// Distancia al pacman en cada dir :S
					}
				}
				direction[index] = ud + lr;
			}
			
			
			// Act try-catch
			// mapInfo = new int[][]
		}
	}

	@Override
	public HashMap<String, Double> getFuzzyValues() {
		HashMap<String,Double> vars = new HashMap<String,Double>();
		for(GHOST g: GHOST.values()) {
			vars.put(g.name()+"distance",   distance[g.ordinal()]);
			vars.put(g.name()+"confidence", confidence[g.ordinal()]);			
		}
		return vars;
	}

}
