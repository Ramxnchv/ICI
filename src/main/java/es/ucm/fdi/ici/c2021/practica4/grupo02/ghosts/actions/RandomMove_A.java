package es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts.actions;

import java.util.Random;

import es.ucm.fdi.ici.fuzzy.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RandomMove_A implements Action {

	private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();
	
	public RandomMove_A() {
	}
	
	@Override
	public MOVE execute(Game game) {
		return allMoves[rnd.nextInt(allMoves.length)];
    }
}