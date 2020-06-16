package main.customs;

import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Range;

public class CustomMedicationRequest {

    private String name;
    private String medication;
    private String status;
    private String quantity;
    private String range;

    public CustomMedicationRequest(MedicationRequest ms)  {
        try {
            this.name = ms.getMedicationCodeableConcept().getCodingFirstRep().getDisplay();
            this.medication = ms.getMedication().getExtensionFirstRep().toString();
            this.status = ms.getStatus().getDisplay();
            if (ms.getDosageInstructionFirstRep().getDoseSimpleQuantity() != null)
                this.quantity = ms.getDosageInstructionFirstRep().getDoseSimpleQuantity().getValue().toString();
            if (ms.getDosageInstructionFirstRep().getDoseRange() != null)
                this.range = ms.getDosageInstructionFirstRep().getDoseRange().getHigh().getValue().toString();
        }
        catch (Exception e) {
        }
    }

    public String getName() {
        return name;
    }

    public String getMedication() {
        return medication;
    }

    public String getStatus() {
        return status;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getRange() {
        return range;
    }
}
