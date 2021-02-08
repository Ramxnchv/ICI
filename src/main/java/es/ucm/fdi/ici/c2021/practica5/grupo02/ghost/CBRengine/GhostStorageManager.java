package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine;

import java.util.Vector;

import es.ucm.fdi.ici.c2021.practica5.grupo02.CBRengine.CachedLinearCaseBase;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.method.retain.StoreCasesMethod;

public class GhostStorageManager {
	
	Game game;
	CachedLinearCaseBase caseBase;
	Vector<CBRCase> buffer;

	private final static int TIME_WINDOW = 5;
	
	public GhostStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBase(CBRCaseBase caseBase)
	{
		this.caseBase = (CachedLinearCaseBase) caseBase;
	}
	
	public void storeCase(CBRCase newCase)
	{			
		this.buffer.add(newCase);
		
		//Check buffer for old cases to store
		if (this.buffer.size()>TIME_WINDOW) {
			CBRCase bCase = this.buffer.remove(0);
			reviseCase(bCase);
		}
	}
	
	private void reviseCase(CBRCase bCase) {
		GhostDescription description = (GhostDescription)bCase.getDescription();
		GhostResult result = (GhostResult) bCase.getResult();
		Integer iniDistToPacman = description.getPacmanIniDist(); // Distancia inicial al pacman
		Integer currentDistToPacman = (int) game.getDistance(game.getGhostCurrentNodeIndex(GHOST.values()[description.getMe()]), game.getPacmanCurrentNodeIndex(), DM.PATH);
		
		
		if (game.getGhostCurrentNodeIndex(GHOST.values()[description.getMe()]) == -1 ||							// Si nos han comido
			currentDistToPacman < iniDistToPacman && game.isGhostEdible(GHOST.values()[description.getMe()]) || // Si la distancia ha disminuido sin que seamos comestibles
			currentDistToPacman > iniDistToPacman && !game.isGhostEdible(GHOST.values()[description.getMe()]))  // Si la distancia ha disminuido siendo comestibles
				result.setScore(0); // La puntuaci�n es 0
		
		Integer puntuacion = Math.abs(iniDistToPacman - currentDistToPacman);
		//Integer puntuacion = iniDistToPacman - currentDistToPacman;
		
		// Si hemos comido al pacman durante la accion aumentamos la puntuacion de ese movimiento.
		if(description.getLives() > game.getPacmanNumberOfLivesRemaining() && currentDistToPacman < 10) puntuacion += 100;
		
		// Si no, puntuamos el caso con la diferencia absoluta entre la distancia al pacman hace N intersecciones y la actual
		result.setScore(puntuacion);
		
		//System.out.printf(description.toString()+"\n");
		//System.out.printf(result.toString()+"\n");
		//System.out.printf("----------------\n");
		
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		StoreCasesMethod.storeCase(this.caseBase, bCase);
	}

	public void close() {
		for(CBRCase oldCase: this.buffer)
			reviseCase(oldCase);
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
