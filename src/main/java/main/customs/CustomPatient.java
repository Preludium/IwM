package main.customs;


import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CustomPatient {
    private String firstName;
    private String lastName;
    private String id;
    private Date birthDate;
    private String birthDateString;
    private String gender;

    public CustomPatient() {

    }

    public CustomPatient(Bundle.BundleEntryComponent p) {
        Patient patient = (Patient) p.getResource();
        List<String> urlList = Arrays.asList(p.getFullUrl().split("/"));
        this.id = urlList.get(urlList.size() - 1);
        if (patient.hasName()) {
            this.firstName = patient.getName().get(0).getGivenAsSingleString();
            this.lastName = patient.getName().get(0).getFamily();
        }
        if (patient.hasGender())
            this.gender = patient.getGender().getDisplay();
        if (patient.hasBirthDate()) {
            this.birthDate = patient.getBirthDate();
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            this.birthDateString = dt.format(patient.getBirthDate());
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getBirthDateString() {
        return birthDateString;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString(){
        return firstName + " " + lastName;
    }
}
