package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost;


import es.ucm.fdi.ici.c2021.practica5.grupo02.Input;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine.GhostDescription;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;

public class GhostInput implements Input {
	
	private final GHOST me;
	private GhostDescription description; 
		
	public GhostInput(GHOST g) {	me = g;	}
	
	@Override
	public void parseInput(Game game) {
		description = new GhostDescription();
		description.setMe(me.ordinal());
		//description.setIniNodeIndex(game.getGhostCurrentNodeIndex(me));
		description.setEdible(game.isGhostEdible(me));
		description.setLevel(game.getCurrentLevel());
		description.setNearestPPill(computeNearestPPill(game));
		description.setPacmanIniDist(computePacmanDistance(game));
		description.setPacmanRelPos(computePacmanRelativePosition(game));
	}

	@Override
	public CBRQuery getQuery() {
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	// Posición relativa del Pacman con respecto al fantasma
	private Integer computePacmanRelativePosition(Game game) {
		return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex(), DM.EUCLID).ordinal();
	}
	
	// Distancia del pacman al fantasma
	private Integer computePacmanDistance(Game game) {
		if (game.getGhostCurrentNodeIndex(me) == -1) return Integer.MAX_VALUE;
		else return (int) game.getDistance(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex(), DM.EUCLID);
	}
	
	// Distancia del pacman a la PPill más cercana
	private Integer computeNearestPPill(Game game) {
		double nearestPPill = Double.MAX_VALUE;
		for (int pill : game.getActivePowerPillsIndices()) nearestPPill = Math.min(nearestPPill, game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH));
		return (int) nearestPPill;
	}
}
