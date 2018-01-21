package it.polito.tdp.seriea.model;

import java.time.LocalDate;

public class Event implements Comparable<Event> {

	public enum EventType {
		SIMULA_PARTITA, 
		MIGRAZIONE_TIFOSI;

	}

	private final LocalDate time;
	private final EventType eventType;
	private final Match match; // in quale stato

	/**
	 * @param time
	 * @param eventType
	 * @param team
	 */
	public Event(LocalDate time, EventType eventType, Match m) {
		super();
		this.time = time;
		this.eventType = eventType;
		this.match = m;
	}

	/**
	 * @return the time
	 */
	public LocalDate getTime() {
		return time;
	}

	/**
	 * @return the eventType
	 */
	public EventType getEventType() {
		return eventType;
	}

	/**
	 * @return the team
	 */
	public Match getMatch() {
		return match;
	}

	@Override
	public int compareTo(Event arg0) {
		// TODO Auto-generated method stub
		return getTime().compareTo(arg0.getTime());
	}

}
