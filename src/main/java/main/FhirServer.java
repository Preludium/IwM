package main;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import main.customs.CustomPatient;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.r4.model.IdType;
import java.util.ArrayList;
import java.util.List;

public class FhirServer {
    private FhirContext ctx;
    private static final String serverBase = "http://hapi.fhir.org/baseDstu3";
    private IGenericClient client;

    public FhirServer(){
        ctx = FhirContext.forDstu3();
        client = ctx.newRestfulGenericClient(serverBase);
    }

    public List<Bundle.BundleEntryComponent> getAllName(){
        Bundle allPatient = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        return allPatient.getEntry();
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

    public ArrayList<CustomPatient> castToCustomPatient(List<Bundle.BundleEntryComponent> patients){
        ArrayList<CustomPatient> patientArrayList = new ArrayList<>();
        for (Bundle.BundleEntryComponent patient : patients) {
            patientArrayList.add(new CustomPatient(patient));
        }
        return patientArrayList;
    }

    public FhirContext getCtx() {
        return ctx;
    }

    public IGenericClient getClient() {
        return client;
    }
}
