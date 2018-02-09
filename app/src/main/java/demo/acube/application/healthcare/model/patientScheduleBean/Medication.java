
package demo.acube.application.healthcare.model.patientScheduleBean;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medication {

    @SerializedName("medicine_name")
    @Expose
    private String medicineName;
    @SerializedName("dosage")
    @Expose
    private Dosage dosage;
    @SerializedName("images")
    @Expose
    private List<Images> images = null;

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Dosage getDosage() {
        return dosage;
    }

    public void setDosage(Dosage dosage) {
        this.dosage = dosage;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

}
