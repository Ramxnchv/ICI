package es.ucm.fdi.ici.c2021.practica5.grupo02.pacman;


import es.ucm.fdi.ici.c2021.practica5.grupo02.Input;
import es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.CBRengine.MsPacManDescription;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;

public class MsPacManInput implements Input {

	Integer nearestGhost;
	Boolean edible;
	Integer nearestPPill;
	Integer score;
	Integer time;
	
	@Override
	public void parseInput(Game game) {
		computeNearestGhost(game);
		computeNearestPPill(game);
		time = game.getTotalTime();
		score = game.getScore();
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		description.setEdibleGhost(edible);
		description.setNearestGhost(nearestGhost);
		description.setNearestPPill(nearestPPill);
		description.setScore(score);
		description.setTime(time);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private void computeNearestGhost(Game game) {
		nearestGhost = Integer.MAX_VALUE;
		edible = false;
		GHOST nearest = null;
		for(GHOST g: GHOST.values()) {
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
		if(nearest!=null)
			edible = game.isGhostEdible(nearest);
	}
	
	private void computeNearestPPill(Game game) {
		nearestPPill = Integer.MAX_VALUE;
		for(int pos: game.getPowerPillIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < nearestGhost)
				nearestPPill = distance;
		}
	}
}
