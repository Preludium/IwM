package main.customs;

import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.exceptions.FHIRException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomObservation {
    private String name;
    private Date startDate;
    private String startDateString;
    private String measure;


    public CustomObservation(Observation o) {
        try{
            name = o.getCode().getText();
            startDate = o.getIssued();
            SimpleDateFormat dt = new SimpleDateFormat("HH:mm dd-MM-yyyy");
            startDateString = dt.format(this.startDate);
            if (o.hasValueQuantity())
                measure = String.format("%.2f ", o.getValueQuantity().getValue()) + o.getValueQuantity().getUnit();
        }
        catch (FHIRException e){
            e.printStackTrace();
        }
    }
}
