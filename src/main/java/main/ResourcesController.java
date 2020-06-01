package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.customs.CustomPatient;

import java.net.URL;
import java.util.ResourceBundle;

public class ResourcesController implements Initializable {
    private CustomPatient patient;
    private Scene patientScene;
    @FXML private Label patientLabel;
    @FXML private Button backButton;
    @FXML private ComboBox<Object> resourceComboBox;

    public ResourcesController() {

    }

    public ResourcesController(CustomPatient patient, Scene patientScene){
        this.patient = patient;
        this.patientScene = patientScene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        patientLabel.setText(patient.getId());
        backButton.setOnAction(event -> {
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(patientScene);
        });
    }
}
