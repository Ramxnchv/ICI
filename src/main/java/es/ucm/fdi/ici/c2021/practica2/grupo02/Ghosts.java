package es.ucm.fdi.ici.c2021.practica2.grupo02;

import java.awt.BorderLayout;
import java.util.EnumMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.actions.*;
import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.transitions.*;
import es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.CompoundState;
//FSM
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.State;
import es.ucm.fdi.ici.fsm.observers.ConsoleFSMObserver;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;

/*
import es.ucm.fdi.ici.practica2.demofsm.ghosts.actions.ChaseAction;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.actions.RunAwayAction;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.transitions.GhostsEdibleTransition;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.transitions.GhostsNotEdibleAndPacManFarPPill;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.transitions.PacManNearPPillTransition;
*/

public class Ghosts extends GhostController {

	EnumMap<GHOST,FSM> fsms;
	
	public Ghosts() {
		
		fsms = new EnumMap<GHOST,FSM>(GHOST.class);
		
		for(GHOST ghost: GHOST.values()) {
			
			FSM fsm = new FSM(ghost.name());
			//fsm.addObserver(new ConsoleFSMObserver(ghost.name() + ""));
			
				FSM aggresive = new FSM("Aggro");
				//aggresive.addObserver(new ConsoleFSMObserver(ghost.name() + "aggresive FSM"));
				
					FSM attack = new FSM("Attack");
					//attack.addObserver(new ConsoleFSMObserver(ghost.name() + ""));
					State cutRoute = new SimpleState("Cut Pacman Route", new FindCutRoute_A(ghost));
					State chase = new SimpleState("Direct chase", new DirectChase_A(ghost));
					attack.add(chase, new NotTooCloseToPacman_T(ghost), cutRoute);
					attack.add(cutRoute, new TooCloseToPacman_T(ghost), chase);
					attack.ready(chase);
						
				
				State attackMachine = new CompoundState("Attack", attack);
				State cautiousRunAway = new SimpleState("Cautious run away", new RunAway_A(ghost));
				
				aggresive.add(attackMachine, new PPill_GhostToPM_T(ghost), cautiousRunAway);
				aggresive.add(cautiousRunAway, new PMNotCloseToPPill_T(ghost), attackMachine);
				aggresive.ready(attackMachine);
			State aggresiveMachine = new CompoundState("Aggresive", aggresive);
				
				FSM defensive = new FSM("Defensive");
				//defensive.addObserver(new ConsoleFSMObserver(ghost.name() + "defensive FSM"));
					
					FSM survive = new FSM("Defensive");
					//survive.addObserver(new ConsoleFSMObserver(ghost.name() + "survive FSM"));
					State runAway = new SimpleState("Direct RunAway", new RunAway_A(ghost));
					State farthest = new SimpleState("Go to farthest node", new FarthestNode_A(ghost));
					survive.add(runAway, new NotTooCloseToPacman_T(ghost), farthest);
					survive.add(farthest, new TooCloseToPacman_T(ghost), runAway);
					survive.ready(runAway);
					
				State surviveMachine = new CompoundState("Survive", survive);
				State earlyChase = new SimpleState("Early chase", new FindCutRoute_A(ghost));
				
				defensive.add(surviveMachine, new ShortEdibleTime_NotTooClose_T(ghost), earlyChase);
				defensive.add(earlyChase, new PacmanAteOtherPPill_T(ghost), surviveMachine);
				defensive.ready(surviveMachine);
			State defensiveMachine = new CompoundState("Defensive", defensive);
			
			State initialState = new SimpleState("Initial state", new RandomMove_A(ghost));
			
			fsm.add(defensiveMachine, new GhostIsInLair_T(ghost), initialState);
			fsm.add(initialState, new GhostLeftLair_T(ghost), aggresiveMachine);
			fsm.add(aggresiveMachine, new GhostEdible_T(ghost), defensiveMachine);	
			fsm.add(defensiveMachine, new GhostNotEdible_T(ghost), aggresiveMachine);
		
			fsm.ready(initialState);
			
			fsms.put(ghost, fsm);
			
//			GraphFSMObserver graphObserver = new GraphFSMObserver(ghost.name());
//			fsm.addObserver(graphObserver);
//			
//			JFrame frame = new JFrame();
//	    	JPanel main = new JPanel();
//	    	main.setLayout(new BorderLayout());
//	    	main.add(graphObserver.getAsPanel(true, null), BorderLayout.CENTER);
//	    	frame.getContentPane().add(main);
//	    	frame.pack();
//	    	frame.setVisible(true);
			
		}
	}
	
	
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);
		
		GhostsInput in = new GhostsInput(game);
		
		for(GHOST ghost: GHOST.values()) {
			FSM fsm = fsms.get(ghost);
			MOVE move = fsm.run(in);
			result.put(ghost, move);
		}
		
		return result;
	}
}
