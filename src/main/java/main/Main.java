package main;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        FhirContext ctx = FhirContext.forDstu2();
//        String serverBase = "http://localhost:8080/baseDstu2";
        String serverBase = "http://hapi.fhir.org/baseDstu2";
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource("sample.fxml")));
        loader.setController(new Controller(client));
        Parent root = loader.load();
        primaryStage.setTitle("Karta Pacjenta");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
