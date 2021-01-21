package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine;

import pacman.game.Constants.MOVE;
import ucm.gaia.jcolibri.cbrcore.Attribute;
//import gaia.jcolibri.cbrcore.Attribute;

public class GhostDescription implements ucm.gaia.jcolibri.cbrcore.CaseComponent {

	Integer id;
	
	// Informacion mia
	int iniNodeIndex;
	// Info del fantasma mas cercano al ghost
	Double closestGhostDist; // No creo que la queramos del todo
	MOVE closestGhostRelPos; // Creo que con esto seria suficiente
	// Otros param de juego
	Integer nearestPPill;
	Double pacmanIniDist;
	MOVE pacmanRelPos;
	// Importantes a la hora de descartar
	Boolean edible;
	Integer level; // Nivel
	
	// Para la solucion
	Double finalDistToPacman; // *score*
	//MOVE finalRelPos;
	Integer movement; // 0, 1, 2, 3, 4 (up, down, left, right, neutral)
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	// MIO
	public int getIniNodeIndex() {
		return iniNodeIndex;
	}

	public void setIniNodeIndex(int nIndex) {
		this.iniNodeIndex = nIndex;
	}
	
	// Closest Ghost
	public Double getClosestGhostDist() {
		return closestGhostDist;
	}

	public void setClosestGhostDist(Double closestDist) {
		this.closestGhostDist = closestDist;
	}
	
	public MOVE getClosestRelPos() {
		return closestGhostRelPos;
	}

	public void setClosestRelPos(MOVE closestRelPos) {
		this.closestGhostRelPos = closestRelPos;
	}
	
	// Pacman y pills
	public Integer getNearestPPill() {
		return nearestPPill;
	}

	public void setNearestPPill(Integer nearestPPill) {
		this.nearestPPill = nearestPPill;
	}
	
	public Double getPacmanIniDist() {
		return pacmanIniDist;
	}

	public void setPacmanIniDist(Double pacmanDist) {
		this.pacmanIniDist = pacmanDist;
	}
	
	public MOVE getPacmanRelPos() {
		return pacmanRelPos;
	}

	public void setPacmanRelPos(MOVE pacRelPos) {
		this.pacmanRelPos = pacRelPos;
	}
	
	// Descartadores
	public Boolean getEdible() {
		return edible;
	}
	
	public void setEdible(Boolean edible) {
		this.edible = edible;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer lv) {
		this.level = lv;
	}
	
	// Solucion
	public Integer getMovement() {
		return movement;
	}

	public void setMovement(Integer mv) {
		this.movement = mv;
	}
	
	public Double getFinalDistToPacman() {
		return finalDistToPacman;
	}

	public void setFinalDistToPacman(Double pacmanDist) {
		this.finalDistToPacman = pacmanDist;
	}

	// Overrides
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostDescription.class);
	}

	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + ", iniDistToPacman=" + pacmanIniDist + ", RelPosToPacman=" + pacmanRelPos 
				+ ", distToClosestGhost=" + closestGhostDist + ", RelPosToClosestGhost=" + closestGhostRelPos 
				+ ", edible=" + edible + ", nearestPPill=" + nearestPPill 
				+ ", level=" + level + ", movement=" + movement + ", finalDistToPacman=" + finalDistToPacman + "]";
	}
}
