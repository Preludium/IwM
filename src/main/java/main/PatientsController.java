package main;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.customs.CustomPatient;
import org.hl7.fhir.dstu3.model.Bundle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;



public class PatientsController implements Initializable {
    @FXML TextField searchField;
    @FXML TableView<CustomPatient> tableView;
    @FXML TableColumn<CustomPatient, String> idColumn;
    @FXML TableColumn<CustomPatient, String> firstNameColumn;
    @FXML TableColumn<CustomPatient, String> lastNameColumn;
    @FXML TableColumn<CustomPatient, String> birthColumn;
    @FXML TableColumn<CustomPatient, String> genderColumn;
    private FhirServer server;
    private ArrayList<CustomPatient> patientList;


    public PatientsController() {
        server = new FhirServer();
        List<Bundle.BundleEntryComponent> bundle = server.getAllName();
        patientList = server.castToCustomPatient(bundle);
    }

    @Override
    public void initialize(URL location, final ResourceBundle resources) {

        ObservableList<CustomPatient> patients = FXCollections.observableArrayList(patientList);
        setUpTableView();

        tableView.setItems(patients);
        tableView.setRowFactory(tv -> {
            final TableRow<CustomPatient> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    CustomPatient rowData = row.getItem();
                    System.out.println(rowData.getId());
                    loadResourcesScene((Stage) tv.getScene().getWindow(), rowData, tv.getScene());
                }
            });
            return row;
        });
    }

    private void setUpTableView() {
        tableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
            final TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        birthColumn.setCellValueFactory(new PropertyValueFactory<>("birthDateString"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
    }

    private void loadResourcesScene(Stage window, CustomPatient patient, Scene patientScene) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource("resources.fxml")));
            loader.setController(new ResourcesController(patient, patientScene));
            Parent root = loader.load();
            window.setScene(new Scene(root, 700, 500));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
