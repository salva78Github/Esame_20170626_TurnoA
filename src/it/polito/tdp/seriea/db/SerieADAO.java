package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.exception.SerieAException;
import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import it.polito.tdp.seriea.model.TeamsCouple;

public class SerieADAO {

	public List<Team> listTeams(Map<String, Team> teams) throws SerieAException {
		String sql = "SELECT team FROM teams";
		Connection c = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Team> result = new ArrayList<>();

		try {
			c = DBConnect.getConnection();
			st = c.prepareStatement(sql);

			rs = st.executeQuery();

			while (rs.next()) {
				Team team = new Team(rs.getString("team"));
				result.add(team);
				teams.put(team.getTeam(), team);
			}

			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SerieAException("Errore nel recupero delle squadre.", e);
		} finally {
			DBConnect.closeResources(c, st, rs);
		}

	}

	public List<TeamsCouple> listTeamsCouple(Map<String, Team> teams) throws SerieAException {
		String sql = "select count(*) as numero_incontri, m.HomeTeam, m.AwayTeam from matches m group by m.HomeTeam, m.AwayTeam order by numero_incontri desc";
		Connection c = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		List<TeamsCouple> result = new ArrayList<>();

		try {
			c = DBConnect.getConnection();
			st = c.prepareStatement(sql);
			rs = st.executeQuery();

			while (rs.next()) {
				TeamsCouple tc = new TeamsCouple(teams.get(rs.getString("m.HomeTeam")),
						teams.get(rs.getString("m.AwayTeam")), rs.getInt("numero_incontri"));
				result.add(tc);
			}

			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SerieAException("Errore nel recupero delle coppie di squadre che si sono affrontate.", e);
		} finally {
			DBConnect.closeResources(c, st, rs);
		}
	}

	public List<Season> listSeasons() throws SerieAException {
		String sql = "SELECT season, description FROM seasons" ;
		Connection c = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Season> result = new ArrayList<>() ;
		
		try {
			c = DBConnect.getConnection() ;
			st = c.prepareStatement(sql) ;
			rs = st.executeQuery() ;
			
			while(rs.next()) {
				result.add( new Season(rs.getInt("season"), rs.getString("description"))) ;
			}
			
			return result ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SerieAException("Errore nel recupero delle stagioni.", e) ;
		} finally{
			DBConnect.closeResources(c, st, rs);
		}
	}
	
	public List<Match> listMatchBySeason(int seasonId, Map<String, Team> teamsMap) throws SerieAException {
		String sql = "select match_id, s.season, s.description, m.div, m.Date, m.HomeTeam, m.AwayTeam, m.FTHG, m.FTAG, m.FTR, m.HTHG, m.HTAG, m.HTR from matches m, seasons s where m.Season = s.season and s.season = ? order by m.date" ;
		Connection c = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<Match> matches = new ArrayList<>() ;
				
		try {
			c = DBConnect.getConnection() ;
			st = c.prepareStatement(sql) ;
			st.setInt(1, seasonId);
			rs = st.executeQuery() ;
			
			while(rs.next()) {
				Match m = new Match(rs.getInt("match_id"), new Season(rs.getInt("s.season"), rs.getString("s.description")), rs.getString("m.div"), rs.getDate("m.Date").toLocalDate(),
						teamsMap.get(rs.getString("m.HomeTeam")), teamsMap.get(rs.getString("m.AwayTeam")), rs.getInt("m.FTHG"), rs.getInt("m.FTAG"),
						 rs.getString("m.FTR"));
				matches.add(m);	
				
			}
			
			return matches ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SerieAException("Errore nel recupero delle partite del campionato serie a " + seasonId + ".", e) ;
		} finally{
			DBConnect.closeResources(c, st, rs);
		}

	}

	public List<Team> listTeamsBySeason(int seasonId, Map<String, Team> teamsMap) throws SerieAException {
		String sql = "select distinct m.HomeTeam team from matches m where m.Season = ? order by m.HomeTeam " ;
		Connection c = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<Team> result = new ArrayList<>() ;
				
		try {
			c = DBConnect.getConnection() ;
			st = c.prepareStatement(sql) ;
			st.setInt(1, seasonId);
			rs = st.executeQuery() ;
			
			while(rs.next()) {
				Team team = new Team(rs.getString("team"));
				result.add( team) ;
				teamsMap.put(team.getTeam(), team) ;
			}
			
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SerieAException("Errore nel recupero delle squadre partecipanti al campionato serie a " + seasonId + ".", e) ;
		} finally{
			DBConnect.closeResources(c, st, rs);
		}

	}


}
