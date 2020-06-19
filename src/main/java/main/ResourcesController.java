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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class ResourcesController implements Initializable {
    private Scene patientScene;
    private FhirServer server;
    private CustomPatient patient;
    private List<CustomObservation> allObservations;
    private List<CustomObservation> observations;
    private List<CustomMedicationRequest> allMedicationRequests;
    private List<CustomMedicationRequest> medicationRequests;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @FXML private TableView tableView = new TableView<>();
    @FXML private Label patientLabel;
    @FXML private Button backButton;
    @FXML private ComboBox<String> resourceComboBox;
    @FXML private ComboBox<String> comboBoxTypeOfElement;
    @FXML private DatePicker datePickerFrom;
    @FXML private DatePicker datePickerTo;
    private List<TableColumn<CustomObservation, String>> observationColumns = new ArrayList<>();;
    private final List<String> observationFields = new ArrayList<>(Arrays.asList("name", "date", "measure", "note"));
    private List<TableColumn<CustomMedicationRequest, String>> medicationRequestColumns = new ArrayList<>();
    private final List<String> medicationRequestFields = new ArrayList<>(Arrays.asList("medication", "date", "status", "quantity"));

    public ResourcesController() {
    }

    public ResourcesController(CustomPatient patient, Scene patientScene, FhirServer server){
        this.patient = patient;
        this.patientScene = patientScene;
        this.server = server;

        List<Bundle.BundleEntryComponent> bundle = server.getEverything(this.patient.getId());
        allObservations = server.getObservations(bundle);
        List<MedicationRequest> reqs = server.getMedicationRequest(bundle);
        allMedicationRequests = server.getCustomMedicationRequest(reqs);
        observations = new ArrayList<>(allObservations);
        medicationRequests = new ArrayList<>(allMedicationRequests);
    }

    private void initializeComboBoxWithTypes(boolean withObservations, boolean withMedicalRequests){
        Set<String> observationTypes = new HashSet<>();
        observationTypes.add("---");
        if (withObservations){
            observations.forEach(obs -> {
                observationTypes.add(obs.getName());
            });
        }
        if (withMedicalRequests){
            medicationRequests.forEach(med -> {
                observationTypes.add(med.getMedication());
            });
        }
        comboBoxTypeOfElement.setItems(FXCollections.observableArrayList(observationTypes));

        comboBoxTypeOfElement.getSelectionModel().select("---");
    }

    private void handleSelectionOfTypeInformationAboutPatient(){
        resourceComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch(newValue) {
                case "Observation":
                    tableView.getColumns().clear();
                    tableView.getItems().clear();
                    tableView.getColumns().setAll(observationColumns);
                    tableView.setItems(FXCollections.observableArrayList(observations));
                    initializeComboBoxWithTypes(true, false);
                    break;
                case "MedicationRequest":
                    tableView.getColumns().clear();
                    tableView.getItems().clear();
                    tableView.getColumns().setAll(medicationRequestColumns);
                    tableView.setItems(FXCollections.observableArrayList(medicationRequests));
                    initializeComboBoxWithTypes(false, true);
                    break;
            }
        });
    }

    private void filterByDate(){
        if (resourceComboBox.getSelectionModel().getSelectedItem() == "Observation") {
            List<CustomObservation> filteredRows = new ArrayList<>();
            List<CustomObservation> listToFilter = new ArrayList<>();
            if(observations.size()>0){
                listToFilter.addAll(observations);
            }else{
                listToFilter.addAll(allObservations);
            }
            listToFilter.forEach(obs -> {
                LocalDate startDate = obs.getDateS().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (dateFrom==null){
                    dateFrom = LocalDate.of(1700, 01, 01);
                }
                if (dateTo==null){
                    dateTo=LocalDate.now();
                }
                if (startDate.isAfter(dateFrom) && startDate.isBefore(dateTo)) {
                    filteredRows.add(obs);
                }
            });
            observations.clear();
            observations.addAll(filteredRows);
            tableView.getColumns().clear();
            tableView.getItems().clear();
            tableView.getColumns().setAll(observationColumns);
            tableView.setItems(FXCollections.observableArrayList(observations));
        }

        if (resourceComboBox.getSelectionModel().getSelectedItem() == "MedicationRequest") {
            List<CustomMedicationRequest> filteredRows = new ArrayList<>();
            List<CustomMedicationRequest> listToFilter = new ArrayList<>();
            if(medicationRequests.size()>0){
                listToFilter.addAll(medicationRequests);
            }else{
                listToFilter.addAll(allMedicationRequests);
            }
            listToFilter.forEach(medReq -> {
                LocalDate startDate = medReq.getDateS().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (dateFrom==null){
                    dateFrom = LocalDate.of(1700, 01, 01);
                }
                if (dateTo==null){
                    dateTo=LocalDate.now();
                }
                if (startDate.isAfter(dateFrom) && startDate.isBefore(dateTo)) {
                    filteredRows.add(medReq);
                }
            });
            medicationRequests.clear();
            medicationRequests.addAll(filteredRows);
            tableView.getColumns().clear();
            tableView.getItems().clear();
            tableView.getColumns().setAll(medicationRequestColumns);
            tableView.setItems(FXCollections.observableArrayList(medicationRequests));
        }

    }

    private void fillObservationsColumns(){
        for (int i = 0; i < observationFields.size(); ++i) {
            observationColumns.add(new TableColumn<>());
            String s1 = observationFields.get(i).substring(0, 1).toUpperCase();
            String nameCapitalized = s1 + observationFields.get(i).substring(1);
            observationColumns.get(i).setText(nameCapitalized);
            observationColumns.get(i).setMinWidth(125);
            observationColumns.get(i).setCellValueFactory(new PropertyValueFactory<>(observationFields.get(i)));
        }
    }

    private void fillMedicationRequestsColumns(){
        for (int i = 0; i < medicationRequestFields.size(); ++i) {
            medicationRequestColumns.add(new TableColumn<>());
            String s1 = medicationRequestFields.get(i).substring(0, 1).toUpperCase();
            String nameCapitalized = s1 + medicationRequestFields.get(i).substring(1);
            medicationRequestColumns.get(i).setText(nameCapitalized);
            medicationRequestColumns.get(i).setMinWidth(125);
            medicationRequestColumns.get(i).setCellValueFactory(new PropertyValueFactory<>(medicationRequestFields.get(i)));
        }
    }

    @FXML
    public void selectTypeOfElement(){
        dateFrom=LocalDate.of(1970,01,01);
        dateTo=LocalDate.now();
        String selectedValue = comboBoxTypeOfElement.getValue();
        try {
            if (resourceComboBox.getSelectionModel().getSelectedItem() == "Observation") {
                List<CustomObservation> filteredRows = new ArrayList<>();
                if (selectedValue.compareTo("---") == 0) {
                    filteredRows.addAll(allObservations);
                } else {
                    allObservations.forEach(obs -> {
                        if (obs.getName().compareTo(selectedValue) == 0) {
                            filteredRows.add(obs);
                        }
                    });
                }
                observations.clear();
                observations.addAll(filteredRows);
                tableView.getColumns().clear();
                tableView.getItems().clear();
                tableView.getColumns().setAll(observationColumns);
                tableView.setItems(FXCollections.observableArrayList(observations));
            }

            if (resourceComboBox.getSelectionModel().getSelectedItem() == "MedicationRequest") {
                List<CustomMedicationRequest> filteredRows = new ArrayList<>();
                if (selectedValue.compareTo("---") == 0) {
                    filteredRows.addAll(allMedicationRequests);
                } else {
                    allMedicationRequests.forEach(obs -> {
                        if (obs.getMedication().compareTo(selectedValue) == 0) {
                            filteredRows.add(obs);
                        }
                    });
                }
                medicationRequests.clear();
                medicationRequests.addAll(filteredRows);
                tableView.getColumns().clear();
                tableView.getItems().clear();
                tableView.getColumns().setAll(medicationRequestColumns);
                tableView.setItems(FXCollections.observableArrayList(medicationRequests));
            }
            filterByDate();
        }catch(Exception e){

        }

    }

    @FXML
    public void changeDateFrom(){
        this.dateFrom = datePickerFrom.getValue();
        filterByDate();
    }

    @FXML
    public void changeDateTo(){
        this.dateTo = datePickerTo.getValue();
        filterByDate();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateFrom=LocalDate.now();
        dateTo=LocalDate.now();
        resourceComboBox.setItems(FXCollections.observableArrayList("MedicationRequest", "Observation"));
        resourceComboBox.setValue("MedicationRequest");
        patientLabel.setText(patient.getId());
        fillObservationsColumns();
        fillMedicationRequestsColumns();

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

        handleSelectionOfTypeInformationAboutPatient();
        initializeComboBoxWithTypes(false, false);
        backButton.setOnAction(event -> {
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(patientScene);
        });
    }

}