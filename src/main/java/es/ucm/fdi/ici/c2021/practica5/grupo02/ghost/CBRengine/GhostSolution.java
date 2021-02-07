package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

public class GhostSolution implements CaseComponent, Cloneable {
	
	Integer id;
	String action;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostSolution.class);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{  
		return super.clone();  
	}
	
	@Override
	public String toString() {
		return "MsPacManSolution [id=" + id + ", action=" + action + "]";
	}  
}
