package es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts;

import java.util.HashMap;

import es.ucm.fdi.ici.fuzzy.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import pacman.game.internal.Node;

public class GhostsInput implements Input {

	double[] distance = {50,50,50,50};
	double[] confidence = {100,100,100,100};
	
	int[][] mapInfo;

	
	
	@Override
	public void parseInput(Game game) {
		for(GHOST g : GHOST.values()) {
			int index = g.ordinal();
			int pos = game.getGhostCurrentNodeIndex(g);
			if(pos != -1) {
				distance[index] = game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
				confidence[index] = 100;
			} else if (confidence[index] > 0) {
				confidence[index]-=.1;
			}
			
			Node[] gr = game.getCurrentMaze().graph;
			
			Node d = game.getCurrentMaze().graph[game.getCurrentMaze().graph.length - 1];
			
			int c = game.getCurrentMaze().graph.length;
			//mapInfo = new int[][]
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
