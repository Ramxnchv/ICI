package es.ucm.fdi.ici.c2021.practica5.grupo02;

import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.PacmanController;


public class ExecutorTest {

    public static void main(String[] args) {
    	Executor executor = new Executor.Builder()
				.setTickLimit(4000)
				.setVisual(true)
				.setScaleFactor(3.0)
				.build();
    	
        //PacmanController pacMan = new HumanController(new KeyBoardInput());
        PacmanController pacMan = new MsPacMan();
        GhostController ghosts = new Ghost();
        
        System.out.println( 
        		executor.runGame(pacMan, ghosts, 40)
        );
        
    }
}
