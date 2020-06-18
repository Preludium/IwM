package main;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import main.customs.*;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.r4.model.IdType;

import java.util.ArrayList;
import java.util.List;

public class FhirServer {
    private final IGenericClient client;

    public FhirServer(){
        FhirContext ctx = FhirContext.forDstu3();
        String serverBase = "http://localhost:8080/baseDstu3";
//        String serverBase = "http://hapi.fhir.org/baseDstu3";

        client = ctx.newRestfulGenericClient(serverBase);
    }

    public List<CustomPatient> getPatients(){
        Bundle allPatient = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        ArrayList<CustomPatient> patientList = new ArrayList<>();
        for (Bundle.BundleEntryComponent patient : allPatient.getEntry())
            patientList.add(new CustomPatient(patient));

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

    public List<CustomMedication> getMedicationss(List<Bundle.BundleEntryComponent> entries){
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

    public List<MedicationRequest> getMedicationRequest(List<Bundle.BundleEntryComponent> entries) {
        List<MedicationRequest> medicationRequestList = new ArrayList<>();
        for (Bundle.BundleEntryComponent e : entries) {
            if (e.getResource() instanceof MedicationRequest) {
                MedicationRequest mr = ((MedicationRequest) e.getResource());
                medicationRequestList.add(mr);
            }
        }
        return medicationRequestList;
    }

    public List<CustomMedicationRequest> getCustomMedicationRequest(List<MedicationRequest> reqs) {
        List<CustomMedicationRequest> medicationRequestList = new ArrayList<>();
        for (MedicationRequest req : reqs) {
            CustomMedicationRequest mmr = new CustomMedicationRequest(req);
            medicationRequestList.add(mmr);
        }
        return medicationRequestList;
    }

    public List<CustomMedication> getMedications(List<MedicationRequest> medReqs) {
        List<CustomMedication> medicationList = new ArrayList<>();

        for(MedicationRequest req : medReqs){
            if (req.getMedication() instanceof CodeableConcept) {
                CodeableConcept medicationCodeable = ((CodeableConcept) req.getMedication());
                String code = medicationCodeable.getCodingFirstRep().getCode();
                Bundle results = client
                        .search()
                        .forResource(Medication.class)
                        .where(new TokenClientParam("form").exactly().code(code))
                        .returnBundle(Bundle.class)
                        .execute();
                for (Bundle.BundleEntryComponent e : results.getEntry()) {
                    if(e.getResource() instanceof Medication) {
                        Medication m = (Medication) e.getResource();
                        medicationList.add(new CustomMedication(m));
                    }
                }
            }
        }
        return medicationList;
    }
}
