package it.polito.tdp.seriea.db;

import java.util.HashMap;
import java.util.List;

import it.polito.tdp.seriea.exception.SerieAException;
import it.polito.tdp.seriea.model.Team;

public class TestSerieADAO {

	public static void main(String[] args) {
		SerieADAO dao = new SerieADAO();

	
		List<Team> teams;
		try {
			teams = dao.listTeams(new HashMap<String, Team>());
			System.out.println(teams);
			System.out.println("Teams # rows: " + teams.size());
		} catch (SerieAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
