package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;
import it.polito.tdp.seriea.exception.SerieAException;

public class Model {

	private final static SerieADAO dao = new SerieADAO();
	private SimpleWeightedGraph<Team, DefaultWeightedEdge> graph;
	private Map<String, Team> teamIdMap;
	private List<Team> teamsList;
	private Map<Season, List<Match>> matches;
	private Map<String, Team> teamBySeasonIdMap;

	public Model() {
		this.teamIdMap = new HashMap<String, Team>();
		this.teamsList = new ArrayList<Team>();
		this.matches = new HashMap<Season, List<Match>>();
		this.teamBySeasonIdMap = new HashMap<String, Team>();
	}

	/**
	 * creare un grafo che rappresenti le partite giocate da ogni coppia di
	 * squadre, in tutte le stagioni disponibili. Il grafo dovrà essere non
	 * orientato e pesato, con i vertici che rappresentino le squadre che hanno
	 * giocato in almeno una stagione, e gli archi il fatto che tali squadre
	 * abbiano giocato insieme almeno una volta. Il peso dell’arco tra due team
	 * deve rappresentare il numero di partite che quelle due squadre hanno
	 * giocato (contando sia le partite di andata che di ritorno, in ogni
	 * stagione).
	 * 
	 * @return
	 * @throws SerieAException
	 */

	public SimpleWeightedGraph<Team, DefaultWeightedEdge> creaGrafo() throws SerieAException {

		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<Team> vertexList = listTeams();
		System.out.println("<creaGrafo> numero vertici/squadre: " + vertexList.size());
		// crea i vertici del grafo
		Graphs.addAllVertices(graph, vertexList);

		// crea gli archi del grafo
		// faccio fare tutto il lavoro al dao
		List<TeamsCouple> matches = dao.listTeamsCouple(this.teamIdMap);
		for (TeamsCouple tc : matches) {
			DefaultWeightedEdge dwe;
			if (this.graph.containsEdge(tc.getT1(), tc.getT2())) {
				dwe = this.graph.getEdge(tc.getT1(), tc.getT2());
				int numberOfMatches = (int) this.graph.getEdgeWeight(dwe);
				numberOfMatches += tc.getNumberOfMatches();
				this.graph.setEdgeWeight(dwe, numberOfMatches);
			} else {
				dwe = graph.addEdge(tc.getT1(), tc.getT2());
				this.graph.setEdgeWeight(dwe, tc.getNumberOfMatches());
			}
		}
		System.out.println("<creaGrafo> numero archi: " + this.graph.edgeSet().size());

		return this.graph;

	}

	public List<Team> listTeams() throws SerieAException {
		if (this.teamsList.size() == 0) {
			this.teamsList = dao.listTeams(this.teamIdMap);
		}

		return this.teamsList;
	}

	public List<TeamsCouple> retrieveMatches(Team t) {
		List<TeamsCouple> tcList = new ArrayList<TeamsCouple>();

		for (DefaultWeightedEdge dwe : this.graph.edgesOf(t)) {
			TeamsCouple tc;
			if (t.equals(this.graph.getEdgeSource(dwe))) {
				tc = new TeamsCouple(this.graph.getEdgeSource(dwe), this.graph.getEdgeTarget(dwe),
						(int) this.graph.getEdgeWeight(dwe));
			} else {
				tc = new TeamsCouple(this.graph.getEdgeTarget(dwe), this.graph.getEdgeSource(dwe),
						(int) this.graph.getEdgeWeight(dwe));
			}
			tcList.add(tc);
		}

		Collections.sort(tcList);
		return tcList;

	}

	public List<Season> listSeasons() throws SerieAException {
		return dao.listSeasons();
	}

	public List<Match> listMatchesBySeason(Season s) throws SerieAException {
		if (this.matches.containsKey(s)) {
			return this.matches.get(s);
		}

		List<Team> teamsBySeasons = dao.listTeamsBySeason(s.getSeason(), teamBySeasonIdMap);
		List<Match> matchesList = dao.listMatchBySeason(s.getSeason(), teamBySeasonIdMap);
		this.matches.put(s, matchesList);
		return matchesList;
	}
	
	 public List<Team> doSimulation(Season s) throws SerieAException {
		Simulator sim = new Simulator(listMatchesBySeason(s));
		sim.load();
		sim.run();
		//List<Team> teamsList = sim.collectStatistics();
		return new ArrayList(teamBySeasonIdMap.values());

	}

	 
	 
}
