package es.ucm.fdi.ici.c2021.practica3.grupo02.pacman.actions;

import java.util.Random;

import es.ucm.fdi.ici.rules.Action;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Respawn_A implements Action{
	//Esta accion realiza un movimiento aleatorio si pacman está en el punto de aparicion
	//para que no siga siempre la misma ruta
	@Override
	public MOVE execute(Game game) {
		int pacman = game.getPacmanCurrentNodeIndex();
		MOVE [] possibleMoves = game.getPossibleMoves(pacman);
		return possibleMoves[(new Random()).nextInt(possibleMoves.length-1)];
	}

	@Override
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}

}
