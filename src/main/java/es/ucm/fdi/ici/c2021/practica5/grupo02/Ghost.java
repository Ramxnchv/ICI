package es.ucm.fdi.ici.c2021.practica5.grupo02;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import es.ucm.fdi.ici.c2021.practica5.grupo02.actions.*;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.GhostActionSelector;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.GhostInput;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine.GhostCBRengine;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine.GhostStorageManager;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import ucm.gaia.jcolibri.exception.ExecutionException;

public class Ghost extends GhostController {

	GhostInput input;
	GhostCBRengine cbrEngine;
	GhostActionSelector actionSelector;
	GhostStorageManager storageManager;
	
	private EnumMap<GHOST, MOVE> controllers;
	
	final static String FILE_PATH = "grupo02/ghost/cbrdata/%s.csv"; //Cuidado!! poner el grupo aqui
	
	public Ghost()
	{
		controllers = new EnumMap<GHOST, MOVE>(GHOST.class);
		this.input = new GhostInput();
		
		List<Action> actions = new ArrayList<Action>();
		actions.add(new DownAction());
		actions.add(new LeftAction());
		actions.add(new UpAction());
		actions.add(new DownAction());

		this.actionSelector = new GhostActionSelector(actions);

		this.storageManager = new GhostStorageManager();
		
		cbrEngine = new GhostCBRengine(actionSelector, storageManager);
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
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		MOVE move;
		for(GHOST g : GHOST.values()) {
			//This implementation only computes a new action when MsPacMan is in a junction. 
			//This is relevant for the case storage policy
			if(!game.isJunction(game.getGhostCurrentNodeIndex(g))) {
				move = MOVE.NEUTRAL;
			}
			else {
				try {
					input.setGhost(g);
					input.parseInput(game);
					actionSelector.setGame(game);
					storageManager.setGame(game);
					cbrEngine.cycle(input.getQuery());
					Action action = cbrEngine.getSolution();
					move = action.execute(game);
				} catch (Exception e) {
					e.printStackTrace();
					move = MOVE.NEUTRAL;
				}
			}
			controllers.put(g, move);
		}
		return controllers;
	}
}
