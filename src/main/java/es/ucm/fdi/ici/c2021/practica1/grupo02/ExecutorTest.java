package es.ucm.fdi.ici.c2021.practica1.grupo02;

import es.ucm.fdi.ici.c2021.practica2.grupo02.MsPacManFSM;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class ExecutorTest {

	public static void main(String[] args) {
		Executor executor = new Executor.Builder()
				.setTickLimit(4000)
				.setVisual(true)
				.setScaleFactor(3.0)
				.build();
					
				PacmanController pacMan = new MsPacManFSM(); //new HumanController(new KeyBoardInput()); //new MsPacManRunAway();//new PacManRandom();
				GhostController ghosts = new Ghosts();//new GhostAggresive();
					
				System.out.println(
				executor.runGame(pacMan, ghosts, 50)
			);
		}

	}
