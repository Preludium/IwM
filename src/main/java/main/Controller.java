package main;

import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    private Patient patient;
    private ArrayList<Observation> observations = new ArrayList<Observation>();
    private ArrayList<Medication> medications = new ArrayList<>();
    private ArrayList<MedicationStatement> medicationStatements = new ArrayList<>();
    IGenericClient client;

    @FXML
    Button searchbtn;

    public Controller(IGenericClient client) {
        this.client = client;
    }

    @Override
    public void initialize(URL location, final ResourceBundle resources) {
        searchbtn.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Bundle results = client
                        .search()
                        .forResource(Patient.class)
                        .returnBundle(Bundle.class)
                        .execute();
                System.out.println(results.getEntry());
            }
        });
    }
}
