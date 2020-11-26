package es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions;

import java.util.Random;

import es.ucm.fdi.ici.fsm.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Respawn_A implements Action{
	//Esta accion realiza un movimiento aleatorio si pacman está en el punto de aparicion
	@Override
	public MOVE execute(Game game) {
		int pacman = game.getPacmanCurrentNodeIndex();
		MOVE [] possibleMoves = game.getPossibleMoves(pacman);
		return possibleMoves[(new Random()).nextInt(possibleMoves.length-1)];
	}

}
