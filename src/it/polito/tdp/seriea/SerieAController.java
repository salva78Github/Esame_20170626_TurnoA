/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.exception.SerieAException;
import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import it.polito.tdp.seriea.model.TeamsCouple;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxSquadra"
	private ChoiceBox<Team> boxSquadra; // Value injected by FXMLLoader

	@FXML // fx:id="boxStagione"
	private ChoiceBox<Season> boxStagione; // Value injected by FXMLLoader

	@FXML // fx:id="btnCalcolaConnessioniSquadra"
	private Button btnCalcolaConnessioniSquadra; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimulaTifosi"
	private Button btnSimulaTifosi; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalizzaSquadre"
	private Button btnAnalizzaSquadre; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	private Model model;

	@FXML
	void doAnalizzaSquadre(ActionEvent event) {
		try {
			this.boxSquadra.setDisable(false);
			this.btnCalcolaConnessioniSquadra.setDisable(false);
			this.boxStagione.setDisable(false);
			this.btnAnalizzaSquadre.setDisable(false);
			this.btnSimulaTifosi.setDisable(false);
			this.boxStagione.getItems().addAll(this.model.listSeasons());
			this.boxSquadra.getItems().addAll(this.model.listTeams());
			this.model.creaGrafo();
			
		} catch (SerieAException e) {
			e.printStackTrace();
			this.txtResult.setText("Errore nel recupero delle squadre o nella ceazione del grafo.");
		}
	}

	@FXML
	void doCalcolaConnessioniSquadra(ActionEvent event) {
		Team t = this.boxSquadra.getValue();
		List<TeamsCouple> tcList = this.model.retrieveMatches(t);
		
		this.txtResult.setText(String.format("Incontri della squadra %s \n\n", tcList.get(0).getT1()));
		for(TeamsCouple tc : tcList){
			this.txtResult.appendText(String.format("%s --> %d \n", tc.getT2(), tc.getNumberOfMatches()));
		}
		
	}

	@FXML
	void doSimulaTifosi(ActionEvent event) {
		Season s = this.boxStagione.getValue();
		try {
			List<Team> teams = this.model.doSimulation(s);
			for(Team t : teams){
				this.txtResult.appendText(String.format("%s --> %d tifosi e %d punti\n", t.getTeam(), t.getNumbersOfSupporters(), t.getPunti()));
			}
			
		} catch (SerieAException e) {
			this.txtResult.setText("Errore nella simulazione");
			e.printStackTrace();
		}
		
		
		
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
		assert boxStagione != null : "fx:id=\"boxStagione\" was not injected: check your FXML file 'SerieA.fxml'.";
		assert btnCalcolaConnessioniSquadra != null : "fx:id=\"btnCalcolaConnessioniSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
		assert btnSimulaTifosi != null : "fx:id=\"btnSimulaTifosi\" was not injected: check your FXML file 'SerieA.fxml'.";
		assert btnAnalizzaSquadre != null : "fx:id=\"btnAnalizzaSquadre\" was not injected: check your FXML file 'SerieA.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

	}

	public void setModel(Model model) {
		this.model= model;
	}
}
