package es.ucm.fdi.ici.c2021.practica5.grupo02;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.MsPacManActionSelector;
import es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.MsPacManInput;
import es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.CBRengine.MsPacManCBRengine;
import es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.CBRengine.MsPacManStorageManager;
import es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.actions.GoToPPillAction;
import es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.actions.RunAwayAction;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import ucm.gaia.jcolibri.exception.ExecutionException;

public class MsPacMan extends PacmanController {

	MsPacManInput input;
	MsPacManCBRengine cbrEngine;
	MsPacManActionSelector actionSelector;
	MsPacManStorageManager storageManager;
	
	final static String FILE_PATH = "cbrdata/grupo02/%s.csv"; //Cuidado!! poner el grupo aquí
	
	public MsPacMan()
	{
		this.input = new MsPacManInput();
		
		List<Action> actions = new ArrayList<Action>();
		actions.add(new GoToPPillAction());
		actions.add(new RunAwayAction());
		this.actionSelector = new MsPacManActionSelector(actions);

		this.storageManager = new MsPacManStorageManager();
		
		cbrEngine = new MsPacManCBRengine(actionSelector, storageManager);
	}
	
	@Override
	public void preCompute(String opponent) {
		cbrEngine.setCaseBaseFile(String.format(FILE_PATH, opponent));
		try {
			cbrEngine.configure();
			cbrEngine.preCycle();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void postCompute() {
		try {
			cbrEngine.postCycle();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		//This implementation only computes a new action when MsPacMan is in a junction. 
		//This is relevant for the case storage policy
		if(!game.isJunction(game.getPacmanCurrentNodeIndex()))
			return MOVE.NEUTRAL;
		
		
		try {
			input.parseInput(game);
			actionSelector.setGame(game);
			storageManager.setGame(game);
			cbrEngine.cycle(input.getQuery());
			Action action = cbrEngine.getSolution();
			return action.execute(game);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MOVE.NEUTRAL;
	}

}
