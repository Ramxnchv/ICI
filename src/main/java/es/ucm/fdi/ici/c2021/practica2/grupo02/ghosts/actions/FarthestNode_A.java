package es.ucm.fdi.ici.c2021.practica2.grupo02.ghosts.actions;

import es.ucm.fdi.ici.fsm.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Maze;
import pacman.game.internal.Node;
import pacman.game.Game;

public class FarthestNode_A implements Action {

	GHOST me;
	
	public FarthestNode_A(GHOST me) {
		super();
		this.me = me;
	}
	
	@Override
	public MOVE execute(Game g) {
		if (g.doesGhostRequireAction(me)) {
			int farthestNodeFromPacman = g.getFarthestNodeIndexFromNodeIndex(g.getPacmanCurrentNodeIndex(), mazeToIndeces(g.getCurrentMaze()), DM.PATH);
			return g.getApproximateNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(me), farthestNodeFromPacman, g.getGhostLastMoveMade(me), DM.PATH);
		}
		
		return null;
	}
	
	private int[] mazeToIndeces(Maze m) {
		int[] indices = new int[m.graph.length];	int i = 0;	
		for (Node n : m.graph) {	indices[i] = n.nodeIndex;	++i;	}
		return indices;
	}

}
