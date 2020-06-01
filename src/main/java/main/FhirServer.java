package main;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import main.customs.CustomMedication;
import main.customs.CustomMedicationStatement;
import main.customs.CustomObservation;
import main.customs.CustomPatient;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.r4.model.IdType;

import java.util.ArrayList;
import java.util.List;

public class FhirServer {
    private FhirContext ctx;
    private final String serverBase = "http://hapi.fhir.org/baseDstu3";
    private IGenericClient client;

    public FhirServer(){
        ctx = FhirContext.forDstu3();
        client = ctx.newRestfulGenericClient(serverBase);
    }

    public List<CustomPatient> getPatients(){
        Bundle allPatient = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        ArrayList<CustomPatient> patientList = new ArrayList<>();
        for (Bundle.BundleEntryComponent patient : allPatient.getEntry()) {
            patientList.add(new CustomPatient(patient));
        }

        return patientList;
    }

    public List<Bundle.BundleEntryComponent> getEverything(String patientID){
        Parameters outParams = client
                .operation()
                .onInstance(new IdType("Patient", patientID))
                .named("$everything")
                .withNoParameters(Parameters.class)
                .useHttpGet()
                .execute();

        List<Bundle.BundleEntryComponent> entries = new ArrayList<>();
        List<Parameters.ParametersParameterComponent> parameters = outParams.getParameter();

        for (Parameters.ParametersParameterComponent parameter : parameters) {
            Bundle bundle = (Bundle) parameter.getResource();
            entries.addAll(bundle.getEntry());
            while (bundle.getLink(Bundle.LINK_NEXT) != null) {
                bundle = client.loadPage().next(bundle).execute();
                entries.addAll(bundle.getEntry());
            }
        }

        return entries;
    }

    public List<CustomMedication> getMedications(List<Bundle.BundleEntryComponent> entries){
        List<CustomMedication> medicationList = new ArrayList<>();
        for (Bundle.BundleEntryComponent e : entries) {
            if(e.getResource() instanceof Medication) {
                Medication m = (Medication)e.getResource();
                medicationList.add(new CustomMedication(m));
            }
        }

        return medicationList;
    }

    public List<CustomMedicationStatement> getMedicationStatements(List<Bundle.BundleEntryComponent> entries){
        List<CustomMedicationStatement> medicationStatementList = new ArrayList<>();
        for (Bundle.BundleEntryComponent e : entries) {
            if(e.getResource() instanceof MedicationStatement) {
                MedicationStatement m = (MedicationStatement)e.getResource();
                medicationStatementList.add(new CustomMedicationStatement(m));
            }
        }

        return medicationStatementList;
    }

    public List<CustomObservation> getObservations(List<Bundle.BundleEntryComponent> entries)  {
        List<CustomObservation> observationList = new ArrayList<>();
        for (Bundle.BundleEntryComponent e : entries) {
            if(e.getResource() instanceof Observation) {
                Observation o = (Observation)e.getResource();
                observationList.add(new CustomObservation(o));
            }
        }

        return observationList;
    }

}
