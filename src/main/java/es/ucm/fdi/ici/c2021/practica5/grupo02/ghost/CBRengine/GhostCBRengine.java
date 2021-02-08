package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import es.ucm.fdi.ici.c2021.practica5.grupo02.*;
import es.ucm.fdi.ici.c2021.practica5.grupo02.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.GhostActionSelector;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import ucm.gaia.jcolibri.method.retrieve.*;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import ucm.gaia.jcolibri.method.retrieve.selection.SelectCases;
import ucm.gaia.jcolibri.util.FileIO;
import ucm.gaia.jcolibri.cbraplications.StandardCBRApplication;
import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;
import ucm.gaia.jcolibri.connector.PlainTextConnector;
import ucm.gaia.jcolibri.exception.ExecutionException;

public class GhostCBRengine implements StandardCBRApplication {

	private String casebaseFile;
	private Action action;
	private GhostActionSelector actionSelector;
	private GhostStorageManager storageManager;

	CustomPlainTextConnector connector;
	CachedLinearCaseBase caseBase;
	NNConfig simConfig;
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2021/practica5/grupo02/ghost/CBRengine/ghostplaintextconfig.xml";

	/**
	 * Simple extension to allow custom case base files. It also creates a new empty file if it does not exist.
	 */
	public class CustomPlainTextConnector extends PlainTextConnector {
		public void setCaseBaseFile(String casebaseFile) {
			super.PROP_FILEPATH = casebaseFile;
			try {
		         File file = new File(casebaseFile);
		         System.out.println(file.getAbsolutePath());
		         if(!file.exists())
		        	 file.createNewFile();
		      } catch(Exception e) {
		         e.printStackTrace();
		      }
		}
	}
	
	
	public GhostCBRengine(GhostActionSelector actionSelector, GhostStorageManager storageManager)
	{
		this.actionSelector = actionSelector;
		this.storageManager = storageManager;
	}
	
	public void setCaseBaseFile(String casebaseFile) {
		this.casebaseFile = casebaseFile;
	}
	
	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new CachedLinearCaseBase();
		
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		connector.setCaseBaseFile(this.casebaseFile);
		this.storageManager.setCaseBase(caseBase);
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		
		//--------------------------------
		simConfig.addMapping(new Attribute("me", GhostDescription.class), new Equal());
		simConfig.addMapping(new Attribute("nearestPPill",GhostDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("pacmanIniDist",GhostDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("pacmanRelPos",GhostDescription.class), new Equal());
		simConfig.addMapping(new Attribute("edible",GhostDescription.class), new Equal());
		simConfig.addMapping(new Attribute("level",GhostDescription.class), new Equal());
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}


	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		if(caseBase.getCases().isEmpty()) {
			this.action = actionSelector.findAction();
		}else {
			//Compute NN
			//Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			Collection<RetrievalResult> eval = customNN(caseBase.getCases(),query);
			
			// This simple implementation only uses 1NN
			// Consider using kNNs with majority voting
			RetrievalResult cases = SelectCases.selectTopKRR(eval, 5).iterator().next();
			
			//-----
			CBRCase mostSimilarCase = cases.get_case();
			double similarity = cases.getEval();
			//------
	
			GhostResult result = (GhostResult) mostSimilarCase.getResult();
			GhostSolution solution = (GhostSolution) mostSimilarCase.getSolution();
			
			//Now compute a solution for the query
			this.action = actionSelector.getAction(solution.getAction());
			
			if(similarity<0.7) //Sorry not enough similarity, ask actionSelector for an action
				this.action = actionSelector.findAction();
			
			else if(result.getScore() == 0) //This was a bad case, ask actionSelector for another one.
				this.action = actionSelector.findAnotherAction(solution.getAction());
		}
		//lastAction = createNewCase(query);
		CBRCase newCase = createNewCase(query);
		this.storageManager.storeCase(newCase);
	}
	
	private Action actionPoll(Collection<RetrievalResult> cases, CaseComponent caseDescription) {
		HashMap<Action, Double> pollResults = new HashMap<Action, Double>();
		for (RetrievalResult r : cases) {
			CBRCase c = r.get_case();
			Action a = actionSelector.getAction(((GhostSolution) c.getSolution()).getAction());
			pollResults.put(a, pollResults.getOrDefault(a, 0.) + ((GhostResult) c.getResult()).getScore() * computeSimilarity(caseDescription, c.getDescription()));
		}
		Action a = null; Double mostVoted = .0;
		for (Entry<Action, Double> e : pollResults.entrySet()) if (e.getValue() > mostVoted) {	a = e.getKey(); mostVoted = e.getValue();	}	
		return a;
	}

	/**
	 * Creates a new case using the query as description, 
	 * storing the action into the solution and 
	 * setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		GhostDescription newDescription = (GhostDescription) query.getDescription();
		GhostResult newResult = new GhostResult();
		GhostSolution newSolution = new GhostSolution();
		int newId = this.caseBase.getCases().size() + storageManager.getPendingCases() + 1;
		newDescription.setId(newId);
		newResult.setId(newId);
		newSolution.setId(newId);
		newSolution.setAction(this.action.getActionId());
		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);
		return newCase;
	}
	
	// Obtener los NN mas parecidos (No hay que tocarlo)
	private Collection<RetrievalResult> customNN(Collection<CBRCase> cases, CBRQuery query) {
		// Parallel stream

		List<RetrievalResult> res = cases.parallelStream()
				.map(c -> new RetrievalResult(c, computeSimilarity(query.getDescription(), c.getDescription())))
		        .collect(Collectors.toList());

		// Sort the result
		res.sort(RetrievalResult::compareTo);
		return res;
	}
	
	// Comparar la similitud entre 2 casos (adaptarlo a nuestra info)
	private Double computeSimilarity(CaseComponent description, CaseComponent description2) {
		GhostDescription _query = (GhostDescription)description;
		GhostDescription _case = (GhostDescription)description2;

		if(_query.getLevel() != _case.getLevel()) return 0.0;
		if(_query.getEdible() != _case.getEdible()) return 0.0;
		
		double simil = 0;
		simil += Math.abs(_query.getPacmanIniDist() - _case.getPacmanIniDist()) * 0.5;
		simil += Math.abs(_query.getNearestPPill() - _case.getNearestPPill()) * 0.25;
		simil += ((_query.getPacmanRelPos() == _case.getPacmanRelPos()) ? 1. :		// Si es la misma posición
			((MOVE.values()[_query.getPacmanRelPos()].opposite().ordinal() == _case.getPacmanRelPos()) ? .0 : 	// Si es la opuesta
				.5)) * 0.25;		// Si difiere pero no es diametralmente opuesta
		
		return simil;
	}
	
	public Action getSolution() {
		return this.action;
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBase.close();
	}

}
