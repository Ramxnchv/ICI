package es.ucm.fdi.ici.c2021.practica2.grupo02;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.MsPacManInput;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions.ChaseGhost_A;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions.ChasePP_A;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions.ChasePill_A;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions.Respawn_A;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.actions.RunAway_A;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.DangerChasingGhost_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.Danger_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.EdibleGhostNear_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.FreeGhostsPath2PP_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.GhostsFarFromPP_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.NearGhostsAndFreeGhostsPath_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.NoDangerAndEdibleGhostNear_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.NoDanger_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.NoEdibleGhosts_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.NoFreeGhostPath2PP_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.NotSpawnPoint_T;
import es.ucm.fdi.ici.c2021.practica2.grupo02.pacman.transitions.SpawnPoint_T;
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
	    	
	    	//FARM
	    	
	    	FSM cfsm1 = new FSM("FARM");
	    	GraphFSMObserver c1observer = new GraphFSMObserver(cfsm1.toString());
	    	cfsm1.addObserver(c1observer);
	    	
	    	SimpleState chasePill = new SimpleState("chasePills", new ChasePill_A());
	    	SimpleState chasePP = new SimpleState("chasePP", new ChasePP_A());
	    	
	    	Transition nearGhosts = new NearGhostsAndFreeGhostsPath_T();
	    	Transition farGhosts = new GhostsFarFromPP_T();
	    	
	    	cfsm1.add(chasePill, nearGhosts, chasePP);
	    	cfsm1.add(chasePP, farGhosts, chasePill);
	    	cfsm1.ready(chasePill);
	    	
	    	CompoundState farm = new CompoundState("FARM", cfsm1);
	    	
	    	//RUNAWAY
	    	
	    	FSM cfsm2 = new FSM("RUNAWAY");
	    	GraphFSMObserver c2observer = new GraphFSMObserver(cfsm2.toString());
	    	cfsm2.addObserver(c2observer);
	    	
	    	SimpleState directRunAway = new SimpleState("directRunAway", new RunAway_A());
	    	SimpleState chasePPRunAway = new SimpleState("chasePP", new ChasePP_A());
	    	
	    	Transition freePath2PP = new FreeGhostsPath2PP_T();
	    	Transition notFreePath2PP = new NoFreeGhostPath2PP_T();
	    	
	    	cfsm2.add(directRunAway, freePath2PP, chasePPRunAway);
	    	cfsm2.add(chasePPRunAway, notFreePath2PP, directRunAway);
	    	cfsm2.ready(directRunAway);
	    	
	    	CompoundState runAway = new CompoundState("Run Away", cfsm2);
	    	
	    	//CHASE NEAREST GHOST
	    	
	    	SimpleState chaseGhost = new SimpleState("Chase Ghost", new ChaseGhost_A());
	    	
	    	Transition edibleGhostNear = new EdibleGhostNear_T();
	    	Transition noEdibleGhosts = new NoEdibleGhosts_T();
	    	
	    	//SPAWN
	    	
	    	SimpleState respawn = new SimpleState("Respawn", new Respawn_A());
	    	Transition spawnPoint = new SpawnPoint_T();
	    	Transition notSpawnPoint = new NotSpawnPoint_T();
	    	
	    	//MAIN MSPACMANFSM
	    	
	    	Transition danger = new Danger_T();
	    	Transition danger2 = new DangerChasingGhost_T();
	    	Transition noDanger = new NoDanger_T();
	    	Transition noDangerAndEdibleGhostNear = new NoDangerAndEdibleGhostNear_T();
	    	
	    	fsm.add(respawn, notSpawnPoint, farm);
	    	fsm.add(runAway, spawnPoint , respawn);
	    	fsm.add(farm, danger, runAway);
	    	fsm.add(runAway, noDanger, farm);
	    	fsm.add(farm, edibleGhostNear, chaseGhost);
	    	fsm.add(chaseGhost, noEdibleGhosts, farm);
	    	fsm.add(runAway, noDangerAndEdibleGhostNear, chaseGhost);
	    	fsm.add(chaseGhost, danger2, runAway);
	    	
	    	
	    	fsm.ready(respawn);
	    	
	    	
//	    	JFrame frame = new JFrame();
//	    	JPanel main = new JPanel();
//	    	main.setLayout(new BorderLayout());
//	    	main.add(observer.getAsPanel(true, null), BorderLayout.CENTER);
//	    	main.add(c1observer.getAsPanel(true, null), BorderLayout.SOUTH);
//	    	main.add(c2observer.getAsPanel(true, null), BorderLayout.NORTH);
//	    	frame.getContentPane().add(main);
//	    	frame.pack();
//	    	frame.setVisible(true);
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
