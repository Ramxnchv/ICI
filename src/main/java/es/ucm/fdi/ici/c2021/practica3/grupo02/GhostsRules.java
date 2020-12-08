package es.ucm.fdi.ici.c2021.practica3.grupo02;

import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2021.practica3.grupo02.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2021.practica3.grupo02.ghosts.actions.DirectChase_A;
import es.ucm.fdi.ici.c2021.practica3.grupo02.ghosts.actions.FarthestNode_A;
import es.ucm.fdi.ici.c2021.practica3.grupo02.ghosts.actions.FillZone_A;
import es.ucm.fdi.ici.c2021.practica3.grupo02.ghosts.actions.RandomMove_A;
import es.ucm.fdi.ici.c2021.practica3.grupo02.ghosts.actions.RunAway_A;
import es.ucm.fdi.ici.rules.Action;
import es.ucm.fdi.ici.rules.Input;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsRules extends GhostController  {

	private static final String RULES_PATH = "src\\main\\java\\es\\ucm\\fdi\\ici\\c2021\\practica3\\grupo02\\";
	
	HashMap<String,Action> map;
	
	EnumMap<GHOST,RuleEngine> ghostRuleEngines;
	
	
	public GhostsRules() {
		
		map = new HashMap<String,Action>();

		map.put("BLINKYchases", new DirectChase_A(GHOST.BLINKY));
		map.put("INKYchases", new DirectChase_A(GHOST.INKY));
		map.put("PINKYchases", new DirectChase_A(GHOST.PINKY));
		map.put("SUEchases", new DirectChase_A(GHOST.SUE));	
		
		map.put("BLINKYrunsAway", new RunAway_A(GHOST.BLINKY));
		map.put("INKYrunsAway", new RunAway_A(GHOST.INKY));
		map.put("PINKYrunsAway", new RunAway_A(GHOST.PINKY));
		map.put("SUErunsAway", new RunAway_A(GHOST.SUE));
		
		map.put("BLINKYrandom", new RandomMove_A(GHOST.BLINKY));
		map.put("INKYrandom", new RandomMove_A(GHOST.INKY));
		map.put("PINKYrandom", new RandomMove_A(GHOST.PINKY));
		map.put("SUErandom", new RandomMove_A(GHOST.SUE));
		
		map.put("BLINKYgoesToFarthestNode", new FarthestNode_A(GHOST.BLINKY));
		map.put("INKYgoesToFarthestNode", new FarthestNode_A(GHOST.INKY));
		map.put("PINKYgoesToFarthestNode", new FarthestNode_A(GHOST.PINKY));
		map.put("SUEgoesToFarthestNode", new FarthestNode_A(GHOST.SUE));
		
		map.put("BLINKYfillsZone", new FillZone_A(GHOST.BLINKY));
		map.put("INKYfillsZone", new FillZone_A(GHOST.INKY));
		map.put("PINKYfillsZone", new FillZone_A(GHOST.PINKY));
		map.put("SUEfillsZone", new FillZone_A(GHOST.SUE));
		
		ghostRuleEngines = new EnumMap<GHOST,RuleEngine>(GHOST.class);
		for(GHOST ghost: GHOST.values())
		{
			String rulesFile = String.format("%s/%srules.clp", RULES_PATH, ghost.name().toLowerCase());
			RuleEngine engine  = new RuleEngine(ghost.name(),rulesFile, map);
			ghostRuleEngines.put(ghost, engine);
			
			//add observer to every Ghost
			//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(ghost.name(), false);
			//engine.addObserver(observer);
		}
		
		//add observer only to BLINKY
		ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(GHOST.BLINKY.name(), true);
		ghostRuleEngines.get(GHOST.BLINKY).addObserver(observer);
		
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		//Process input
		Input input = new GhostsInput(game);
		//load facts
		input.parseInput();
		//reset the rule engines
		for(RuleEngine engine: ghostRuleEngines.values()) {
			engine.reset();
			engine.assertFacts(input.getFacts());
		}
		
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);		
		for(GHOST ghost: GHOST.values())
		{
			RuleEngine engine = ghostRuleEngines.get(ghost);
			MOVE move = engine.run(game);
			result.put(ghost, move);
		}
		
		return result;
	}

}

