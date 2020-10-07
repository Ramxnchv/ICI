package es.ucm.fdi.ici.c2021.practica0.grupo02;


import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManRunAway extends PacmanController{

	private int ghostPositions[];
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		ghostPositions = new int[4];
		
		int i=0;
		for (GHOST ghostType : GHOST.values()) {
			ghostPositions[i]=game.getGhostCurrentNodeIndex(ghostType);
			i++;
		}
		
		int value = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), ghostPositions, DM.PATH);
		
		GHOST nearest=null;
		
		for (GHOST ghostType : GHOST.values()) {
			if(game.getGhostCurrentNodeIndex(ghostType)==value) {
				nearest = ghostType;
				break;
			}
		}
		
		
		return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearest), game.getPacmanLastMoveMade(), DM.PATH);
	}

}
