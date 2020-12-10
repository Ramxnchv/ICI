package es.ucm.fdi.ici.c2021.practica3.grupo02;

import java.util.HashMap;

import es.ucm.fdi.ici.c2021.practica3.grupo02.pacman.MsPacManInput;
import es.ucm.fdi.ici.c2021.practica3.grupo02.pacman.actions.ChaseGhost_A;
import es.ucm.fdi.ici.c2021.practica3.grupo02.pacman.actions.ChasePP_A;
import es.ucm.fdi.ici.c2021.practica3.grupo02.pacman.actions.ChasePill_A;
import es.ucm.fdi.ici.c2021.practica3.grupo02.pacman.actions.Respawn_A;
import es.ucm.fdi.ici.c2021.practica3.grupo02.pacman.actions.RunAway_A;
import es.ucm.fdi.ici.rules.Action;
import es.ucm.fdi.ici.rules.Input;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PacManRules extends PacmanController{
	
	private static final String RULES_PATH = "src\\main\\java\\es\\ucm\\fdi\\ici\\c2021\\practica3\\grupo02\\";
	HashMap<String,Action> map;
	private RuleEngine pacmanEngine;
	
	public PacManRules() {
		map = new HashMap<String,Action>();
		
		map.put("PACMANrespawn", new Respawn_A());
		map.put("PACMANchaseGhost", new ChaseGhost_A());
		map.put("PACMANchasePP", new ChasePP_A());
		map.put("PACMANchasePill", new ChasePill_A());
		map.put("PACMANrunAway", new RunAway_A());
		
		String rulesFile = String.format("%s/%srules.clp", RULES_PATH, "pacman");
		pacmanEngine  = new RuleEngine("pacman",rulesFile, map);
		
		//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver("pacman", true);
		//pacmanEngine.addObserver(observer);
		
	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {

		//Process input
		Input input = new MsPacManInput(game);
		
		//load facts
		input.parseInput();
		
		//reset the rule engine
		pacmanEngine.reset();
		pacmanEngine.assertFacts(input.getFacts());
		
		MOVE move = pacmanEngine.run(game);
		
		return move;
	}

}
