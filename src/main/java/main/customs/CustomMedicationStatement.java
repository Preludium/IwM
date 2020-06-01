package main.customs;

import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.exceptions.FHIRException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomMedicationStatement {
    private String name;
    private String taken;
    private String dosage;
    private Date startDate;
    private String startDateString;
    private Date endDate;
    private String endDateString;


    public CustomMedicationStatement(MedicationStatement ms)  {
        try{
            this.name = ms.getMedicationCodeableConcept().getCodingFirstRep().getDisplay();
            this.taken =  ms.getTaken().getDisplay();
            this.dosage=  ms.getDosageFirstRep().getDoseSimpleQuantity().getValue().toString() + ms.getDosageFirstRep().getDoseSimpleQuantity().getUnit();
            SimpleDateFormat dt = new SimpleDateFormat("HH:mm dd-MM-yyyy");
            this.startDate = ms.getEffectivePeriod().getStart();
            this.startDateString = dt.format(this.startDate);
            this.endDate =  ms.getEffectivePeriod().getEnd();
            this.endDateString = dt.format(this.endDate);

        } catch (FHIRException e) {
            e.printStackTrace();
        }
    }
}
