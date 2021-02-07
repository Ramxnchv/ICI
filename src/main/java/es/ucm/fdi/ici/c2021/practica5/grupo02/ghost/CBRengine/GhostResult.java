package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

public class GhostResult implements CaseComponent, Cloneable {

	Integer id;
	Integer score;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostResult.class);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{  
		return super.clone();  
	}

	@Override
	public String toString() {
		return "GhostResult [id=" + id + ", score=" + score + "]";
	} 
}
