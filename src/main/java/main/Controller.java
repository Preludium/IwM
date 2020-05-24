package main;

import ca.uhn.fhir.model.dstu2.resource.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    private FhirServer server;
    private ArrayList<CustomPatient> patientList;
    @FXML Button searchbtn;
    @FXML TableView<CustomPatient> tableView;
    @FXML TableColumn<CustomPatient, String> firstNameColumn;
    @FXML TableColumn<CustomPatient, String> lastNameColumn;

    public Controller() {

    }

    @Override
    public void initialize(URL location, final ResourceBundle resources) {
        server = new FhirServer();
        List<Bundle.Entry> bundle = server.getAllName();
        ArrayList<CustomPatient> patientList = server.castToCustomPatient(bundle);
        ObservableList<CustomPatient> patients = FXCollections.observableArrayList();
        patients.addAll(patientList);
        tableView.setItems(patients);

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<CustomPatient, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<CustomPatient, String>("lastName"));

    }
}
