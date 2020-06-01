package main;

import com.google.common.collect.Lists;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.customs.CustomMedication;
import main.customs.CustomMedicationStatement;
import main.customs.CustomObservation;
import main.customs.CustomPatient;
import org.hl7.fhir.dstu3.model.Bundle;

import java.net.URL;
import java.util.*;

// TODO: edit columns names
// TODO: resources empty lists :(

public class ResourcesController implements Initializable {
    private Scene patientScene;
    private FhirServer server;
    private CustomPatient patient;
    private List<CustomObservation> observations;
    private List<CustomMedication> medications;
    private List<CustomMedicationStatement> medicationStatements;

    @FXML private TableView tableView = new TableView<>();
    @FXML private Label patientLabel;
    @FXML private Button backButton;
    @FXML private ComboBox<String> resourceComboBox;
    private List<TableColumn<CustomMedication, String>> medicationColumns = new ArrayList<>();;
    private List<String> medicationFields = new ArrayList(Arrays.asList("name", "code", "manufacturer", "description"));
    private List<TableColumn<CustomMedicationStatement, String>> medicationStatementColumns = new ArrayList<>();
    private List<String> medicationStatementFields = new ArrayList(Arrays.asList("name", "taken", "dosage", "startDateString", "endDateString"));
    private List<TableColumn<CustomObservation, String>> observationColumns = new ArrayList<>();;
    private List<String> observationFields = new ArrayList(Arrays.asList("name", "startDateString", "measure"));

    public ResourcesController() {

    }

    public ResourcesController(CustomPatient patient, Scene patientScene, FhirServer server){
        this.patient = patient;
        this.patientScene = patientScene;
        this.server = server;

        List<Bundle.BundleEntryComponent> bundle = server.getEverything(this.patient.getId());
        medications = server.getMedications(bundle);
        medicationStatements = server.getMedicationStatements(bundle);
        observations = server.getObservations(bundle);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> comboBoxList = FXCollections.observableArrayList("Medication", "MedicationStatement", "Observation");
        resourceComboBox.setItems(comboBoxList);
        resourceComboBox.setValue(comboBoxList.get(0));
        patientLabel.setText(patient.getId());
        for (int i = 0; i < medicationFields.size(); ++i) {
            medicationColumns.add(new TableColumn<>());
            String s1 = medicationFields.get(i).substring(0, 1).toUpperCase();
            String nameCapitalized = s1 + medicationFields.get(i).substring(1);
            medicationColumns.get(i).setText(nameCapitalized);
            medicationColumns.get(i).setMinWidth(125);
            medicationColumns.get(i).setCellValueFactory(new PropertyValueFactory<>(medicationFields.get(i)));
        }
        for (int i = 0; i < medicationStatementFields.size(); ++i) {
            medicationStatementColumns.add(new TableColumn<>());
            String s1 = medicationStatementFields.get(i).substring(0, 1).toUpperCase();
            String nameCapitalized = s1 + medicationStatementFields.get(i).substring(1);
            medicationStatementColumns.get(i).setText(nameCapitalized);
            medicationStatementColumns.get(i).setMinWidth(125);
            medicationStatementColumns.get(i).setCellValueFactory(new PropertyValueFactory<>(medicationStatementFields.get(i)));
        }
        for (int i = 0; i < observationFields.size(); ++i) {
            observationColumns.add(new TableColumn<>());
            String s1 = observationFields.get(i).substring(0, 1).toUpperCase();
            String nameCapitalized = s1 + observationFields.get(i).substring(1);
            observationColumns.get(i).setText(nameCapitalized);
            observationColumns.get(i).setMinWidth(125);
            observationColumns.get(i).setCellValueFactory(new PropertyValueFactory<>(observationFields.get(i)));
        }
        tableView.getColumns().setAll(medicationColumns);
        tableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
            final TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });
        tableView.setItems(FXCollections.observableArrayList(medications));
        resourceComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch(newValue) {
                case "Medication":
                    tableView.getColumns().clear();
                    tableView.getColumns().setAll(medicationColumns);
                    tableView.setItems(FXCollections.observableArrayList(medications));
                    break;
                case "MedicationStatement":
                    tableView.getColumns().clear();
                    tableView.getColumns().setAll(medicationStatementColumns);
                    tableView.setItems(FXCollections.observableArrayList(medicationStatements));
                    break;
                case "Observation":
                    tableView.getColumns().clear();
                    tableView.getColumns().setAll(observationColumns);
                    tableView.setItems(FXCollections.observableArrayList(observations));
                    break;
            }
        });
        backButton.setOnAction(event -> {
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(patientScene);
        });
    }
}
