package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost;


import es.ucm.fdi.ici.c2021.practica5.grupo02.Input;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine.GhostDescription;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;

public class GhostInput implements Input {
	//private GhostDescription description;
	
	Integer nearestPPill;
	Integer pcIniDist;
	Integer pcRelativePos;
	Integer level;
	Integer me;
	Boolean edible;
		
	public GhostInput(GHOST g) { me = g.ordinal(); }
	
	@Override
	public void parseInput(Game game) {
		//description.setIniNodeIndex(game.getGhostCurrentNodeIndex(me));
		edible = game.isGhostEdible(GHOST.values()[me]);
		level = game.getCurrentLevel();
		computePacmanDistance(game);
		computeNearestPPill(game);
		computePacmanRelativePosition(game);
		
		//description.setLives(game.getPacmanNumberOfLivesRemaining());
	}

	@Override
	public CBRQuery getQuery() {
		GhostDescription description = new GhostDescription();
		description.setMe(me);
		description.setEdible(edible);
		description.setLevel(level);
		description.setNearestPPill(nearestPPill);
		description.setPacmanIniDist(pcIniDist);
		description.setPacmanRelPos(pcRelativePos);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	// Posición relativa del Pacman con respecto al fantasma
	private void computePacmanRelativePosition(Game game) {
		pcRelativePos = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.values()[me]), game.getPacmanCurrentNodeIndex(), DM.EUCLID).ordinal();
	}
	
	// Distancia del pacman al fantasma
	private void computePacmanDistance(Game game) {
		if (game.getGhostCurrentNodeIndex(GHOST.values()[me]) == -1) pcIniDist = Integer.MAX_VALUE;
		else pcIniDist = (int) game.getDistance(game.getGhostCurrentNodeIndex(GHOST.values()[me]), game.getPacmanCurrentNodeIndex(), DM.EUCLID);
	}
	
	// Distancia del pacman a la PPill más cercana
	private void computeNearestPPill(Game game) {
		nearestPPill = Integer.MAX_VALUE;
		for (int pill : game.getActivePowerPillsIndices()) nearestPPill = (int) Math.min(nearestPPill, game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH));
	}
}
