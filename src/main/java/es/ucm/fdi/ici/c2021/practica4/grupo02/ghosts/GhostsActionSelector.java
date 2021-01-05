package es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts;

import java.util.HashMap;
import java.util.Random;

import es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts.actions.ChaseAction;
import es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts.actions.RunawayAction;
import es.ucm.fdi.ici.fuzzy.Action;
import es.ucm.fdi.ici.fuzzy.ActionSelector;
import pacman.game.Constants.MOVE;


public class GhostsActionSelector implements ActionSelector {
	
	private final Double NOTHERNMOST_VALUE = 100.;
	private final Double SOUTHERNMOST_VALUE = 50.;
	private final Double EASTERNMOST_VALUE = 25.;
	private final Double WESTERNMOST_VALUE = 75.;

	private final Double RUN_AWAY_LIMIT = 20.0;

	@Override
	public Action selectAction(HashMap<String, Double> fuzzyOutput) {
		Double runAway = fuzzyOutput.get("runAway");
		Double nextTurn = fuzzyOutput.get("nextTurn");
		
		if(runAway> this.RUN_AWAY_LIMIT)
			return new RunawayAction(parseNextMove(nextTurn));
		else
			return new ChaseAction(parseNextMove(nextTurn));
		
	}

	private MOVE parseNextMove(Double n) {
		if (n > SOUTHERNMOST_VALUE - 25./2 && n < SOUTHERNMOST_VALUE + 25./2) return MOVE.DOWN;
		else if (n > EASTERNMOST_VALUE - 25./2 && n < EASTERNMOST_VALUE + 25./2) return MOVE.RIGHT;
		else if (n > WESTERNMOST_VALUE - 25./2 && n < WESTERNMOST_VALUE + 25./2) return MOVE.LEFT;
		else if (n > NOTHERNMOST_VALUE - 25./2 || n < (NOTHERNMOST_VALUE + 25./2) % 100) return MOVE.UP;
		else return MOVE.values()[(new Random()).nextInt() % MOVE.values().length];
	}
	
}
