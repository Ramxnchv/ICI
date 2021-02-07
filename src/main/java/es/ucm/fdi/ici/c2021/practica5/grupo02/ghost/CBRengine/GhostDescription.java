package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

public class GhostDescription implements CaseComponent {

	Integer id;
	
	// Informacion mia
	Integer me;
	Integer iniNodeIndex;
	
	// Info del fantasma mas cercano al ghost
	//Double closestGhostDist; // No creo que la queramos del todo
	//MOVE closestGhostRelPos; // Creo que con esto seria suficiente
	// Otros param de juego
	Integer nearestPPill;
	Integer pacmanIniDist;
	Integer pacmanRelPos;
	// Importantes a la hora de descartar
	Boolean edible;
	Integer level; // Nivel
	
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
	
	public Integer getMe() {
		return me;
	}

	public void setMe(Integer g) {
		this.me = g;
	}	
	
	/*
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
	*/
	
	// Pacman y pills
	public Integer getNearestPPill() {
		return nearestPPill;
	}

	public void setNearestPPill(Integer nearestPPill) {
		this.nearestPPill = nearestPPill;
	}
	
	public Integer getPacmanIniDist() {
		return pacmanIniDist;
	}

	public void setPacmanIniDist(Integer pacmanDist) {
		this.pacmanIniDist = pacmanDist;
	}
	
	public Integer getPacmanRelPos() {
		return pacmanRelPos;
	}

	public void setPacmanRelPos(Integer pacRelPos) {
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

	// Overrides
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostDescription.class);
	}

	@Override
	public String toString() {
		return "GhostDescription [id=" + id + ", iniDistToPacman=" + pacmanIniDist + ", RelPosToPacman=" + pacmanRelPos 
				/*+ ", distToClosestGhost=" + closestGhostDist + ", RelPosToClosestGhost=" + closestGhostRelPos*/ 
				+ ", edible=" + edible + ", nearestPPill=" + nearestPPill 
				+ ", level=" + level + "]";
	}
}
