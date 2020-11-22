package es.ucm.fdi.ici.c2021.practica2.grupo02;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.MsPacManInput;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions.ChaseGhost_A;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions.ChasePP_A;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions.ChasePill_A;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions.RunAway_A;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.ChaseGhostConditions_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.ChasePPConditions_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.ChasePillConditions_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.ExistAtLeast1FreeGhostsPath_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.NotExistAnyFreeGhostsPath_T;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.Input;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManFSM extends PacmanController {

		FSM fsm;
		
		public MsPacManFSM() {
	    	fsm = new FSM("MsPacMan");
	    	
	    	GraphFSMObserver observer = new GraphFSMObserver(fsm.toString());
	    	fsm.addObserver(observer);
	    	
	    	SimpleState runAway = new SimpleState("runAway", new RunAway_A());
	    	
	    	Transition notFreeGhostsPath = new NotExistAnyFreeGhostsPath_T();
	    	Transition freeGhostsPath = new ExistAtLeast1FreeGhostsPath_T();
	    	
	    	
	    	FSM cfsm1 = new FSM("Chase");
	    	GraphFSMObserver c1observer = new GraphFSMObserver(cfsm1.toString());
	    	cfsm1.addObserver(c1observer);
	    	
	    	SimpleState chasePill = new SimpleState("chasePill", new ChasePill_A());
	    	SimpleState chasePP = new SimpleState("chasePP", new ChasePP_A());
	    	SimpleState chaseGhost = new SimpleState("chaseGhost", new ChaseGhost_A());
	    	
	    	Transition chasePPConditions = new ChasePPConditions_T();
	    	Transition chaseGhostConditions = new ChaseGhostConditions_T();
	    	Transition chasePillConditions = new ChasePillConditions_T();
	    	
	    	cfsm1.add(chasePill, chasePPConditions, chasePP);
	    	cfsm1.add(chasePP, chaseGhostConditions, chaseGhost);
	    	cfsm1.add(chaseGhost, chasePillConditions, chasePill);
	    	cfsm1.ready(chasePill);
	    	
	    	CompoundState chase = new CompoundState("chase", cfsm1);
	    	
	    	fsm.add(chase, notFreeGhostsPath, runAway);
	    	fsm.add(runAway, freeGhostsPath, chase);
	    	
	    	fsm.ready(chase);
	    	
	    	
	    	JFrame frame = new JFrame();
	    	JPanel main = new JPanel();
	    	main.setLayout(new BorderLayout());
	    	main.add(observer.getAsPanel(true, null), BorderLayout.CENTER);
	    	frame.getContentPane().add(main);
	    	frame.pack();
	    	frame.setVisible(true);
		}
		
		
		public void preCompute(String opponent) {
	    		fsm.reset();
	    }
		
		
		
	    /* (non-Javadoc)
	     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	     */
	    @Override
	    public MOVE getMove(Game game, long timeDue) {
	    	return fsm.run(new MsPacManInput(game));
	    }
}
