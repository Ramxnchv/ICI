package es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine;

import java.io.File;
import java.util.Collection;

import es.ucm.fdi.ici.c2021.practica5.grupo02.*;
import es.ucm.fdi.ici.c2021.practica5.grupo02.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.GhostActionSelector;
import ucm.gaia.jcolibri.method.retrieve.*;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import ucm.gaia.jcolibri.method.retrieve.selection.SelectCases;
import ucm.gaia.jcolibri.cbraplications.StandardCBRApplication;
import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;
import ucm.gaia.jcolibri.exception.ExecutionException;

public class GhostCBRengine implements StandardCBRApplication {

	private String casebaseFile;
	private Action action;
	private GhostActionSelector actionSelector;
	private GhostStorageManager storageManager;
	
	private CBRCase lastAction;

	CustomPlainTextConnector connector;
	CachedLinearCaseBase caseBase;
	NNConfig simConfig;
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2021/practica5/grupo02/ghost/CBRengine/ghostplaintextconfig.xml"; //Cuidado!! poner el grupo aquí

	/**
	 * Simple extension to allow custom case base files. It also creates a new empty file if it does not exist.
	 */
	public class CustomPlainTextConnector extends ucm.gaia.jcolibri.connector.PlainTextConnector {
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
	
	
	public GhostCBRengine(es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.GhostActionSelector actionSelector, GhostStorageManager storageManager)
	{
		this.actionSelector = actionSelector;
		this.storageManager = storageManager;
	}
	
	public void setCaseBaseFile(String casebaseFile) {
		this.casebaseFile = casebaseFile;
	}
	
	@Override
	public void configure() throws ucm.gaia.jcolibri.exception.ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new CachedLinearCaseBase();
		
		connector.initFromXMLfile(ucm.gaia.jcolibri.util.FileIO.findFile(CONNECTOR_FILE_PATH));
		connector.setCaseBaseFile(this.casebaseFile);
		this.storageManager.setCaseBase(caseBase);
		
		simConfig = new ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		
		//--------------------------------
		// Falta la pos de los otros ghosts
		simConfig.addMapping(new Attribute("nearestPPill",GhostDescription.class), new Interval(15000));
		simConfig.addMapping(new Attribute("iniDistToPacman",GhostDescription.class), new Interval(4000));
		simConfig.addMapping(new Attribute("level",GhostDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("movement",GhostDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("edible",GhostDescription.class), new Equal());
		
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
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			
			// This simple implementation only uses 1NN
			// Consider using kNNs with majority voting
			RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
			
			//-----
			CBRCase mostSimilarCase = first.get_case();
			double similarity = first.getEval();
			//------
	
			GhostResult result = (GhostResult) mostSimilarCase.getResult();
			GhostSolution solution = (GhostSolution) mostSimilarCase.getSolution();
			
			//Now compute a solution for the query
			this.action = actionSelector.getAction(solution.getAction());
			
			if(similarity<0.7) //Sorry not enough similarity, ask actionSelector for an action
				this.action = actionSelector.findAction();
			
			else if(result.getScore()<0) //This was a bad case, ask actionSelector for another one.
				this.action = actionSelector.findAnotherAction(solution.getAction());
		}
		//lastAction = createNewCase(query);
		CBRCase newCase = createNewCase(query);
		this.storageManager.storeCase(newCase);
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
		int newId = this.caseBase.getCases().size();
		newId+= storageManager.getPendingCases();
		newDescription.setId(newId);
		newResult.setId(newId);
		newSolution.setId(newId);
		newSolution.setAction(this.action.getActionId());
		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);
		return newCase;
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
