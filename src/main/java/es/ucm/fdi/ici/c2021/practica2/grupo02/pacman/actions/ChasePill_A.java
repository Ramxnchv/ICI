package es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions;

import es.ucm.fdi.ici.fsm.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChasePill_A implements Action{

	private int pill;
	
	@Override
	public MOVE execute(Game game) {
		
		//La accion de chasePill consiste en seleccionar el camino mas corto hacia la Pill mas cercana
		pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.PATH);
		game.isPillStillAvailable(pill);
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), pill, game.getPacmanLastMoveMade(), DM.PATH);
		
	}
	
	
}
