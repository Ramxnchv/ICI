package es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts;


import es.ucm.fdi.ici.fuzzy.ActionSelector;
import es.ucm.fdi.ici.fuzzy.FuzzyEngine;
import es.ucm.fdi.ici.fuzzy.observers.ConsoleFuzzyEngineObserver;
import pacman.controllers.POGhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * The Class RandomGhosts.
 */
public final class GhostsFuzzy extends POGhostController {
    
    private static final String RULES_PATH = "src/main/java/es/ucm/fdi/ici/c2021/practica4/grupo02/ghosts/";
    private FuzzyEngine fuzzyEngine;
    private GhostsInput input;
	
	public GhostsFuzzy() {
		ActionSelector actionSelector = new GhostsActionSelector();
		input = new GhostsInput();
		 
		 ConsoleFuzzyEngineObserver observer = new ConsoleFuzzyEngineObserver("Ghosts","GhostsRules");
			fuzzyEngine = new FuzzyEngine("Ghosts",RULES_PATH+"ghosts.fcl","FuzzyGhosts",actionSelector);
			fuzzyEngine.addObserver(observer);
	}
	
	@Override
	public MOVE getMove(GHOST ghost, Game game, long timeDue) {
		input.parseInput(game);
		return fuzzyEngine.run(input.getFuzzyValues(),game);
	}
}