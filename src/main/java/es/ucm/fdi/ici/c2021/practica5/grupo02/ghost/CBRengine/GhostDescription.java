package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

public class GhostDescription implements CaseComponent {

	Integer id;
	
	// Informacion mia
	Integer me;
	// Otros parámetros del juego
	Integer nearestPPill;
	Integer pacmanIniDist;
	Integer pacmanRelPos;
	// Importantes a la hora de descartar
	Boolean edible;
	Integer level;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMe() {
		return me;
	}

	public void setMe(Integer me) {
		this.me = me;
	}

	public Integer getNearestPPill() {
		return nearestPPill;
	}

	public void setNearestPPill(Integer nearestPPill) {
		this.nearestPPill = nearestPPill;
	}

	public Integer getPacmanIniDist() {
		return pacmanIniDist;
	}

	public void setPacmanIniDist(Integer pacmanIniDist) {
		this.pacmanIniDist = pacmanIniDist;
	}

	public Integer getPacmanRelPos() {
		return pacmanRelPos;
	}

	public void setPacmanRelPos(Integer pacmanRelPos) {
		this.pacmanRelPos = pacmanRelPos;
	}

	public Boolean getEdible() {
		return edible;
	}

	public void setEdible(Boolean edible) {
		this.edible = edible;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostDescription.class);
	}

	@Override
	public String toString() {
		return "GhostDescription [id=" + id + ", me=" + me + ", nearestPPill=" + nearestPPill + ", pacmanIniDist="
				+ pacmanIniDist + ", pacmanRelPos=" + pacmanRelPos + ", edible=" + edible + ", level=" + level + "]";
	}

}
