package main.customs;

import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.exceptions.FHIRException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomObservation {
    private String name;
    private String date;
    private String measure;
    private String note;

    public CustomObservation(Observation o) {
        if (o.hasCode())
            name = o.getCode().getText();
        if (o.hasIssued()) {
            SimpleDateFormat dt = new SimpleDateFormat("HH:mm dd-MM-yyyy");
            date = dt.format(o.getIssued());
        }
        if (o.hasValueQuantity())
            measure = String.format("%.2f ", o.getValueQuantity().getValue()) + o.getValueQuantity().getUnit();
        if (o.hasComment())
            this.note = o.getComment();
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getMeasure() {
        return measure;
    }

    public String getNote() {
        return note;
    }
}
