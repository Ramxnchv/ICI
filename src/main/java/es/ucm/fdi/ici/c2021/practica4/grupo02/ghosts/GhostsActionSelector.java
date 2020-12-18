package es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts;

import java.util.HashMap;

import es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts.actions.DirectChase_A;
import es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts.actions.RandomMove_A;
import es.ucm.fdi.ici.fuzzy.Action;
import es.ucm.fdi.ici.fuzzy.ActionSelector;


public class GhostsActionSelector implements ActionSelector {

	private final Double RUN_AWAY_LIMIT = 20.0;

	@Override
	public Action selectAction(HashMap<String, Double> fuzzyOutput) {
		Double runAway = fuzzyOutput.get("runAway");
		return new RandomMove_A();
		/*
		if(runAway> this.RUN_AWAY_LIMIT)
			return new RunAwayAction();
		else
			return new GoToPPillAction();
		*/
	}

}
