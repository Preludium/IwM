package main;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.IdType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FhirServer {
    private FhirContext ctx;
    private static final String serverBase = "http://hapi.fhir.org/baseDstu2";
    private IGenericClient client;

    public FhirServer(){
        ctx = FhirContext.forDstu2();
        client = ctx.newRestfulGenericClient(serverBase);
    }

    public List<Bundle.Entry> getAllName(){
        Bundle allPatient = this.getClient()
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        return allPatient.getEntry();
    }

    public List<Bundle.Entry> getEverything(String patientID){
        Parameters outParams = client
                .operation()
                .onInstance(new IdType("Patient", patientID))
                .named("$everything")
                .withNoParameters(Parameters.class)
                .useHttpGet()
                .execute();

        List<Bundle.Entry> entries = new LinkedList<>();
        List<Parameters.Parameter> parameters = outParams.getParameter();

        for (Parameters.Parameter parameter : parameters) {
            Bundle bundle = (Bundle) parameter.getResource();
            entries.addAll(bundle.getEntry());
            while (bundle.getLink(Bundle.LINK_NEXT) != null) {
                // load next page
                bundle = client.loadPage().next(bundle).execute();
                entries.addAll(bundle.getEntry());
            }
        }

        return entries;
    }

    public ArrayList<CustomPatient> castToCustomPatient(List<Bundle.Entry> patients){
        ArrayList<CustomPatient> patientArrayList = new ArrayList<>();
        for (Bundle.Entry patient : patients) {
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
