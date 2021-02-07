package es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.CBRengine;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

public class MsPacManDescription implements CaseComponent {

	Integer id;
	
	Integer dist2nearestEdibleGhost;
	
	Integer dist2nearestNotEdibleGhost; 
	
	Integer dist2nearestPP;
	
	//Set<MOVE> posiblesDirs;
	
	Integer pacmanLastMove;
	
	Integer score;
	
	Integer level;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDist2nearestEdibleGhost() {
		return dist2nearestEdibleGhost;
	}

	public void setDist2nearestEdibleGhost(Integer dist2nearestEdibleGhost) {
		this.dist2nearestEdibleGhost = dist2nearestEdibleGhost;
	}

	public Integer getDist2nearestNotEdibleGhost() {
		return dist2nearestNotEdibleGhost;
	}

	public void setDist2nearestNotEdibleGhost(Integer dist2nearestNotEdibleGhost) {
		this.dist2nearestNotEdibleGhost = dist2nearestNotEdibleGhost;
	}

	public Integer getDist2nearestPP() {
		return dist2nearestPP;
	}

	public void setDist2nearestPP(Integer dist2nearestPP) {
		this.dist2nearestPP = dist2nearestPP;
	}

//	public Set<MOVE> getPosiblesDirs() {
//		return posiblesDirs;
//	}

//	public void setPosiblesDirs(Set<MOVE> posiblesDirs) {
//		this.posiblesDirs = posiblesDirs;
//	}

	public Integer getPacmanLastMove() {
		return pacmanLastMove;
	}

	public void setPacmanLastMove(Integer pacmanLastMove) {
		this.pacmanLastMove = pacmanLastMove;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer Score) {
		score = Score;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescription.class);
	}

	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + ", score=" + score + ", dist2nearestEdibleGhost=" + dist2nearestEdibleGhost + ", dist2nearestNotEdibleGhost="
				+ dist2nearestNotEdibleGhost + ", dist2nearestPP=" + dist2nearestPP + ", level=" + level
				+ ", pacmanLastMove=" + pacmanLastMove + "]";
	}
	
	

}
