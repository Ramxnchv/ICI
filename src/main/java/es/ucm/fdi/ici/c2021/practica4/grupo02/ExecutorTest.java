package es.ucm.fdi.ici.c2021.practica4.grupo02;


import es.ucm.fdi.ici.c2021.practica4.grupo02.ghosts.GhostsFuzzy;
import es.ucm.fdi.ici.c2021.practica4.grupo02.pacman.MsPacManFuzzy;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;


public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setGhostPO(true)
                .setPacmanPO(true)
                .setVisual(true)
                .setScaleFactor(3.0)
                .build();

        PacmanController pacMan = new MsPacManFuzzy();
        GhostController ghosts = new GhostsFuzzy();
        
        /*
        GhostController[] ghosts = new GhostController[4];
        for(GHOST g : GHOST.values()) {
        	ghosts[g.ordinal()] = new GhostsRandom();
        }
        */
        
        System.out.println( 
        		executor.runGame(pacMan, ghosts, 40)
        );
        
    }
}
