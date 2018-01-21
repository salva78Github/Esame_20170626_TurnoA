package it.polito.tdp.seriea.model;

public class Team {

	private String team;
	private int numbersOfSupporters = 1000;
	private int punti = 0;
	
	public Team(String team) {
		this.team = team;
	}

	/**
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}


	
	
	/**
	 * @return the numbersOfSupporters
	 */
	public int getNumbersOfSupporters() {
		return numbersOfSupporters;
	}

	public void incrementNumbersOfSupporters(int numbersOfSupporters) {
		this.numbersOfSupporters += numbersOfSupporters;
	}

	public void decrementNumbersOfSupporters(int numbersOfSupporters) {
		this.numbersOfSupporters -= numbersOfSupporters;
	}

	
	/**
	 * @return the punti
	 */
	public int getPunti() {
		return punti;
	}

	/**
	 * @param punti the punti to set
	 */
	public void addPunti(int punti) {
		this.punti += punti;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return team;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((team == null) ? 0 : team.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		return true;
	}

}
