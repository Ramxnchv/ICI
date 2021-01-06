package es.ucm.fdi.ici.c2021.practica4.grupo02.pacman.actions;

import es.ucm.fdi.ici.fuzzy.Action;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class ChasePPAction implements Action {
	

	@Override
	public MOVE execute(Game game) {
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(),DM.PATH) , game.getPacmanLastMoveMade(), DM.PATH);
	}
}
