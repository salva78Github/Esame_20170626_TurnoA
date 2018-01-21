package it.polito.tdp.seriea.model;

import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.seriea.model.Event.EventType;

public class Simulator {

	// Simulation parameters
	private static final int NUMERO_TIFOSI_PARTENZA = 1000;

	// World model
	private List<Match> matches;

	// Measures of Interest
	private List<Team> teams;

	// Event queue
	private PriorityQueue<Event> queue;

	/**
	 * @param matches
	 */
	public Simulator(List<Match> matches) {
		this.queue = new PriorityQueue<>();
		this.matches = matches;
	}

	public void load() {
		for (Match m : this.matches) {
			Event e = new Event(m.getDate(), EventType.SIMULA_PARTITA, m);
			this.queue.add(e);
		}
	}

	public void run() {

		while (!queue.isEmpty()) {
			Event e = queue.poll();
			System.out.println(e);

			switch (e.getEventType()) {

			case SIMULA_PARTITA:
				/*
				 * Il risultato di ogni partita “simulata” dipenderà dal
				 * risultato “storico”, ossia quello presente nel database,
				 * corretto da un fattore che tenga conto del numero di tifosi.
				 * Infatti, in presenza di un maggior numero di tifosi è più
				 * facile vincere. In una ipotetica partita della squadra A
				 * contro la squadra B, sia TA il numero di tifosi della squadra
				 * A e TB il numero di tifosi della squadra B. Se TA<TB (TA/TB è
				 * un numero compreso tra 0 e 1) simulare che, con una
				 * probabilità pari a (1-TA/TB), la squadra A (quella con meno
				 * tifosi) faccia un goal in meno rispetto al risultato storico.
				 * Nel caso in cui TA>TB, allora sarà la squadra B ad avere una
				 * probabilità (1- TB/TA) di fare un goal in meno. e.
				 * 
				 * Al termine di ogni partita, alcuni dei tifosi della squadra
				 * che ha perso decidono di passare alla squadra vincente. La
				 * percentuale di tifosi che cambierà squadra sarà proporzionale
				 * allo scarto-reti moltiplicato per un fattore P (si ipotizzi
				 * P=10). Ad esempio, se una il risultato fosse A-B=3-1, lo
				 * scarto reti sarebbe pari a 2, ed il 2*10=20% dei tifosi della
				 * squadra B passerebbe alla squadra A. La squadra che vince la
				 * partita non perde alcun tifoso (ma ne guadagnerà). In caso di
				 * pareggio, nessun tifoso si sposta.
				 */
				Match m = e.getMatch();
				int homeSupporters = m.getHomeTeam().getNumbersOfSupporters();
				int awaySupporters = m.getAwayTeam().getNumbersOfSupporters();
				double probability;
				double random;

				int homeScore = m.getFthg();
				int awayScore = m.getFtag();
				if (homeSupporters < awaySupporters) {

					probability = 1 - (homeSupporters / awaySupporters);
					random = Math.random();
					if (random <= probability) {
						homeScore -= 1;
					} else {
						awayScore -= 1;
					}

				}
				if (homeSupporters > awaySupporters) {
					probability = 1 - (awaySupporters / homeSupporters);
					random = Math.random();
					if (random <= probability) {
						awayScore -= 1;
					} else {
						homeScore -= 1;
					}

				}
				/*
				 * * Al termine di ogni partita, alcuni dei tifosi della squadra
				 * che ha perso decidono di passare alla squadra vincente. La
				 * percentuale di tifosi che cambierà squadra sarà proporzionale
				 * allo scarto-reti moltiplicato per un fattore P (si ipotizzi
				 * P=10). Ad esempio, se una il risultato fosse A-B=3-1, lo
				 * scarto reti sarebbe pari a 2, ed il 2*10=20% dei tifosi della
				 * squadra B passerebbe alla squadra A. La squadra che vince la
				 * partita non perde alcun tifoso (ma ne guadagnerà). In caso di
				 * pareggio, nessun tifoso si sposta.
				 * 
				 */
				if (homeScore > awayScore) {
					m.getHomeTeam().addPunti(3);

					m.getAwayTeam()
							.decrementNumbersOfSupporters((int) ((homeScore - awayScore) * 10 * awaySupporters / 100));
					m.getHomeTeam()
							.incrementNumbersOfSupporters((int) ((homeScore - awayScore) * 10 * awaySupporters / 100));
				} else if (awayScore > homeScore) {
					m.getAwayTeam().addPunti(3);
					m.getHomeTeam()
							.decrementNumbersOfSupporters((int) ((awayScore - homeScore) * 10 * homeSupporters / 100));
					m.getAwayTeam()
							.incrementNumbersOfSupporters((int) ((awayScore - homeScore) * 10 * homeSupporters / 100));
				} else {
					m.getHomeTeam().addPunti(1);
					m.getAwayTeam().addPunti(1);

				}

				break;

			case MIGRAZIONE_TIFOSI:
				break;

			}
		}

	}

}
