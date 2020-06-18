package main.customs;

import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Range;

public class CustomMedicationRequest {

    private String name;
    private String status;
    private String quantity;

    public CustomMedicationRequest(MedicationRequest ms)  {
        try {
            this.name = ms.getMedicationCodeableConcept().getCodingFirstRep().getDisplay();
            this.status = ms.getStatus().getDisplay();
            if (ms.getDosageInstructionFirstRep().getDoseSimpleQuantity() != null)
                this.quantity = ms.getDosageInstructionFirstRep().getDoseSimpleQuantity().getValue().toString();
            else
                this.quantity = "1";
        }
        catch (Exception e) {
        }
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getQuantity() {
        return quantity;
    }

}
