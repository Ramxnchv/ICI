package es.ucm.fdi.ici.c2021.practica4.grupo02;

import es.ucm.fdi.ici.c2021.practica4.grupo02.pacman.MsPacManFuzzy;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController{
	
	private MsPacManFuzzy controller;
	
	public MsPacMan() {
		controller = new MsPacManFuzzy();
	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		return controller.getMove(game,timeDue);
	}

}
