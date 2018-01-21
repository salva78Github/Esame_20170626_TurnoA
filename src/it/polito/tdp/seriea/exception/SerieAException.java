package it.polito.tdp.seriea.exception;

public class SerieAException extends Exception {
	private static final long serialVersionUID = 1815820402266704446L;

	/**
	 * @param message
	 * @param e
	 */
	public SerieAException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * @param e
	 */
	public SerieAException(String e) {
		super(e);
	}

}
