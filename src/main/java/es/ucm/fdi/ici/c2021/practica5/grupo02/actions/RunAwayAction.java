package es.ucm.fdi.ici.c2021.practica5.grupo02.actions;


import java.util.Random;

import es.ucm.fdi.ici.c2021.practica5.grupo02.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayAction implements Action {
    
	private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();
	public RunAwayAction() {
	}
	
	@Override
	public MOVE execute(Game game) {
		return allMoves[rnd.nextInt(allMoves.length)];
    }
	
	@Override
	public String getActionId() {
		return "Run Away";
	}            
}
