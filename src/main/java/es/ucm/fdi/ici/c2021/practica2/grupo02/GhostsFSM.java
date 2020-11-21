package es.ucm.fdi.ici.c2021.practica2.grupo02;

import java.util.EnumMap;

import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.actions.*;
import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions.*;
import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.GhostsInput;

//FSM
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.observers.ConsoleFSMObserver;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;

/*
import es.ucm.fdi.ici.practica2.demofsm.ghosts.actions.ChaseAction;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.actions.RunAwayAction;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.transitions.GhostsEdibleTransition;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.transitions.GhostsNotEdibleAndPacManFarPPill;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.transitions.PacManNearPPillTransition;
*/

public class GhostsFSM extends GhostController {

	EnumMap<GHOST,FSM> fsms;
	public GhostsFSM() {
		fsms = new EnumMap<GHOST,FSM>(GHOST.class);
		for(GHOST ghost: GHOST.values()) {
			FSM fsm = new FSM(ghost.name());
			fsm.addObserver(new ConsoleFSMObserver(ghost.name()));
			fsm.addObserver(new GraphFSMObserver(ghost.name()));

			
			//Acciones
			SimpleState chase = new SimpleState("chase", new Chase_A(ghost));
			SimpleState runAway = new SimpleState("runAway", new RunAway_A(ghost));
			
			//Transiciones
			GhostEdible_T edible = new GhostEdible_T(ghost);
			PPill_GhostToPM_T near = new PPill_GhostToPM_T(ghost);
			GhostsNotEdibleAndPacManFarPPill_T toChaseTransition = new GhostsNotEdibleAndPacManFarPPill_T(ghost);
			
			//Actualizacion fsm
			fsm.add(chase, edible, runAway);
			fsm.add(chase, near, runAway);
			fsm.add(runAway, toChaseTransition, chase);
			
			// Init fsm
			fsm.ready(chase);
			fsms.put(ghost, fsm);
		}
	}
	
	
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);
		
		GhostsInput in = new GhostsInput(game);
		
		for(GHOST ghost: GHOST.values())
		{
			FSM fsm = fsms.get(ghost);
			MOVE move = fsm.run(in);
			result.put(ghost, move);
		}
		
		return result;
		
	
		
	}

}
