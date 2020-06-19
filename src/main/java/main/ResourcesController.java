package main;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.customs.*;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.MedicationRequest;

import java.net.URL;
import java.util.*;


public class ResourcesController implements Initializable {
    private Scene patientScene;
    private FhirServer server;
    private CustomPatient patient;
    private List<CustomObservation> observations;
    private List<CustomMedicationRequest> medicationRequests;

    @FXML private TableView tableView = new TableView<>();
    @FXML private Label patientLabel;
    @FXML private Button backButton;
    @FXML private ComboBox<String> resourceComboBox;
    private List<TableColumn<CustomObservation, String>> observationColumns = new ArrayList<>();;
    private final List<String> observationFields = new ArrayList<>(Arrays.asList("name", "date", "measure", "note"));
    private List<TableColumn<CustomObservation, String>> medicationRequestColumns = new ArrayList<>();
    private final List<String> medicationRequestFields = new ArrayList<>(Arrays.asList("medication", "date", "status", "quantity"));

    public ResourcesController() {
    }

    public ResourcesController(CustomPatient patient, Scene patientScene, FhirServer server){
        this.patient = patient;
        this.patientScene = patientScene;
        this.server = server;

        List<Bundle.BundleEntryComponent> bundle = server.getEverything(this.patient.getId());
        observations = server.getObservations(bundle);
        List<MedicationRequest> reqs = server.getMedicationRequest(bundle);
        medicationRequests = server.getCustomMedicationRequest(reqs);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceComboBox.setItems(FXCollections.observableArrayList("MedicationRequest", "Observation"));
        resourceComboBox.setValue("MedicationRequest");
        patientLabel.setText(patient.getId());
        for (int i = 0; i < observationFields.size(); ++i) {
            observationColumns.add(new TableColumn<>());
            String s1 = observationFields.get(i).substring(0, 1).toUpperCase();
            String nameCapitalized = s1 + observationFields.get(i).substring(1);
            observationColumns.get(i).setText(nameCapitalized);
            observationColumns.get(i).setMinWidth(125);
            observationColumns.get(i).setCellValueFactory(new PropertyValueFactory<>(observationFields.get(i)));
        }
        for (int i = 0; i < medicationRequestFields.size(); ++i) {
            medicationRequestColumns.add(new TableColumn<>());
            String s1 = medicationRequestFields.get(i).substring(0, 1).toUpperCase();
            String nameCapitalized = s1 + medicationRequestFields.get(i).substring(1);
            medicationRequestColumns.get(i).setText(nameCapitalized);
            medicationRequestColumns.get(i).setMinWidth(125);
            medicationRequestColumns.get(i).setCellValueFactory(new PropertyValueFactory<>(medicationRequestFields.get(i)));
        }
        tableView.getColumns().setAll(medicationRequestColumns);
        tableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
            final TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });
        tableView.setItems(FXCollections.observableArrayList(medicationRequests));

        resourceComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch(newValue) {
                case "Observation":
                    tableView.getColumns().clear();
                    tableView.getItems().clear();
                    tableView.getColumns().setAll(observationColumns);
                    tableView.setItems(FXCollections.observableArrayList(observations));
                    break;
                case "MedicationRequest":
                    tableView.getColumns().clear();
                    tableView.getItems().clear();
                    tableView.getColumns().setAll(medicationRequestColumns);
                    tableView.setItems(FXCollections.observableArrayList(medicationRequests));
                    break;
            }
        });
        backButton.setOnAction(event -> {
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(patientScene);
        });
    }


}
