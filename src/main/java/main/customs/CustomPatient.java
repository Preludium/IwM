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
        try {
            Patient patient = (Patient) p.getResource();
            List<String> urlList = Arrays.asList(p.getFullUrl().split("/"));
            this.id = urlList.get(urlList.size() - 1);
            this.firstName = patient.getName().isEmpty() ? "" : patient.getName().get(0).getGivenAsSingleString();
            this.lastName = patient.getName().isEmpty() ? "" : patient.getName().get(0).getFamily();
            this.gender = patient.getGender() != null ? patient.getGender().getDisplay() : "";
            if (patient.getBirthDate() != null) {
                this.birthDate = patient.getBirthDate();
                SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
                this.birthDateString = dt.format(patient.getBirthDate());
            }
        }
        catch (Exception e) {
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthDateString() {
        return birthDateString;
    }

    public void setBirthDateString(String birthDateString) {
        this.birthDateString = birthDateString;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
