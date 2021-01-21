package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine;

import ucm.gaia.jcolibri.cbrcore.Attribute;
//import gaia.jcolibri.cbrcore.Attribute;

public class GhostDescription implements ucm.gaia.jcolibri.cbrcore.CaseComponent {

	Integer id;
	
	// Falta la info de los demas ghost
	Integer nearestPPill;
	Boolean edible;
	Double iniDistToPacman;
	Integer level; // Nivel
	Integer movement; // 0, 1, 2, 3, 4 (up, down, left, right, neutral)
	Double finalDistToPacman; // *score*
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getNearestPPill() {
		return nearestPPill;
	}

	public void setNearestPPill(Integer nearestPPill) {
		this.nearestPPill = nearestPPill;
	}
	
	public Boolean getEdible() {
		return edible;
	}
	
	public void setEdible(Boolean edible) {
		this.edible = edible;
	}

	public Double getIniDistToPacman() {
		return iniDistToPacman;
	}

	public void setIniDistToPacman(Double pacmanDist) {
		this.iniDistToPacman = pacmanDist;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer lv) {
		this.level = lv;
	}
	
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

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostDescription.class);
	}

	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + ", iniDistToPacman=" + iniDistToPacman + ", edible=" + edible + ", nearestPPill="
				+ nearestPPill + ", level=" + level + ", movement=" + movement + ", finalDistToPacman=" + finalDistToPacman + "]";
	}
}
