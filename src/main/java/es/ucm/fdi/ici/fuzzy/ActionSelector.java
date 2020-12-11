package es.ucm.fdi.ici.fuzzy;

import java.util.HashMap;

public interface ActionSelector {

	public abstract Action selectAction(HashMap<String, Double> fuzzyOutput);
}
