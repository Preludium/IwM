package main.customs;

import org.hl7.fhir.dstu3.model.MedicationRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomMedicationRequest {

    private String medication;
    private String status;
    private Date dateS;
    private String date;
    private String quantity;

    public CustomMedicationRequest(MedicationRequest ms)  {
        if (ms.hasMedicationCodeableConcept())
            this.medication = ms.getMedicationCodeableConcept().getCodingFirstRep().getDisplay();
        if (ms.hasStatus())
            this.status = ms.getStatus().getDisplay();
        if (ms.hasAuthoredOn()) {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            this.dateS = ms.getAuthoredOn();
            this.date = dt.format(dateS);
        }
        if (ms.getDosageInstructionFirstRep().hasDoseSimpleQuantity())
            this.quantity = ms.getDosageInstructionFirstRep().getDoseSimpleQuantity().getValue().toString();
    }

    public String getMedication() {
        return medication;
    }

    public String getDate() {
        return date;
    }

    public Date getDateS() {
        return dateS;
    }

    public String getStatus() {
        return status;
    }

    public String getQuantity() {
        return quantity;
    }

}
