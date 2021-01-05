package es.ucm.fdi.ici.c2021.practica4.grupo02;

import java.util.EnumMap;

import es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts.GhostsFuzzy;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	
	private EnumMap<GHOST, GhostsFuzzy> controllers;
	
	public Ghosts() {
		controllers = new EnumMap<GHOST, GhostsFuzzy>(GHOST.class);
		for (GHOST g : GHOST.values()) {
			controllers.put(g, new GhostsFuzzy());
		}
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		for (GHOST g : GHOST.values()) moves.put(g, controllers.get(g).getMove(g, game, timeDue));
		return moves;
	}

}
