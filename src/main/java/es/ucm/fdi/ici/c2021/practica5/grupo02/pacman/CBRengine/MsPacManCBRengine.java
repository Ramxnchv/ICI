package es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.CBRengine;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import es.ucm.fdi.ici.c2021.practica5.grupo02.*;
import es.ucm.fdi.ici.c2021.practica5.grupo02.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine.GhostDescription;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine.GhostResult;
import es.ucm.fdi.ici.c2021.practica5.grupo02.ghost.CBRengine.GhostSolution;
import es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.MsPacManActionSelector;
import pacman.game.Constants.MOVE;
import ucm.gaia.jcolibri.method.retrieve.*;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
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
import ucm.gaia.jcolibri.exception.ExecutionException;

public class MsPacManCBRengine implements StandardCBRApplication {

	private String casebaseFile;
	private Action action;
	private MsPacManActionSelector actionSelector;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CachedLinearCaseBase caseBase;
	NNConfig simConfig;
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2021/practica5/grupo02/pacman/CBRengine/pacmanplaintextconfig.xml"; //Cuidado!! poner el grupo aquí

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
	
	
	public MsPacManCBRengine(es.ucm.fdi.ici.c2021.practica5.grupo02.pacman.MsPacManActionSelector actionSelector, MsPacManStorageManager storageManager)
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
		simConfig.addMapping(new Attribute("dist2nearestEdibleGhost",MsPacManDescription.class), new Interval(15000));
		simConfig.addMapping(new Attribute("dist2nearestNotEdibleGhost",MsPacManDescription.class), new Interval(4000));
		simConfig.addMapping(new Attribute("dist2nearestPP",MsPacManDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("dist2nearestPill",MsPacManDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("level",MsPacManDescription.class), new Interval(50));
		simConfig.addMapping(new Attribute("score",MsPacManDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("pacmanLastMove",MsPacManDescription.class), new Interval(4));
		
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
			RetrievalResult first = SelectCases.selectTopKRR(eval, 5).iterator().next();
			
			//-----
			CBRCase mostSimilarCase = first.get_case();
			double similarity = first.getEval();
			//------
	
			MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
			MsPacManSolution solution = (MsPacManSolution) mostSimilarCase.getSolution();
			
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
		MsPacManDescription newDescription = (MsPacManDescription) query.getDescription();
		MsPacManResult newResult = new MsPacManResult();
		MsPacManSolution newSolution = new MsPacManSolution();
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
		MsPacManDescription _query = (MsPacManDescription)description;
		MsPacManDescription _case = (MsPacManDescription)description2;
		
		if(_query.getLevel() != _case.getLevel()) return 0.0;
		
		double simil = 0;
		
		simil += Math.abs(_query.getDist2nearestNotEdibleGhost() - _case.getDist2nearestNotEdibleGhost()) * 0.4;
		simil += Math.abs(_query.getDist2nearestEdibleGhost() - _case.getDist2nearestEdibleGhost()) * 0.15;
		simil += Math.abs(_query.getDist2nearestPP() - _case.getDist2nearestPP()) * 0.15;
		simil += Math.abs(_query.getDist2nearestPill() - _case.getDist2nearestPill()) * 0.15;
		simil += _query.getPacmanLastMove().equals(_case.getPacmanLastMove()) ? 0.15 : 0 ;
		//simil += _query.getPosiblesDirs().containsAll(_case.getPosiblesDirs()) ? 0.1 : 0;
		
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
