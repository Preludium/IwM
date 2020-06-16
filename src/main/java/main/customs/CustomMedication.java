package main.customs;


import org.hl7.fhir.dstu3.model.Medication;

public class CustomMedication {
    private String name;
    private String code;
    private String manufacturer;
    private String description;

    public CustomMedication(Medication m) {
        try {
            name = m.getCode().getText();
            code = m.getCode().getCodingFirstRep().getCode();
            manufacturer = m.getManufacturer().getDisplay();
            description = m.getCode().getCoding().get(1).getDisplay();
        }
        catch (Exception e) {
        }
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getDescription() {
        return description;
    }
}
