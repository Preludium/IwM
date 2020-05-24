package main;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomPatient {
    private String firstName;
    private String lastName;
    private String id;
    private Date birthDate;
    private String birthDateString;
    private String gender;

    public CustomPatient() {

    }

    public CustomPatient(Bundle.Entry p) {
        Patient patient = (Patient) p.getResource();
        this.id = p.getFullUrl().split("/")[p.getFullUrl().split("/").length - 1];
        this.firstName = patient.getName().isEmpty() ? "none" : patient.getName().get(0).getGivenAsSingleString();
        this.lastName = patient.getName().isEmpty() ? "none" : patient.getName().get(0).getFamilyAsSingleString();
        this.gender = patient.getGender() == null ? "nogender" : patient.getGender();
        this.birthDate = patient.getBirthDate();

        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        this.birthDateString = patient.getBirthDate() == null ? "none" : dt.format(patient.getBirthDate());
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
