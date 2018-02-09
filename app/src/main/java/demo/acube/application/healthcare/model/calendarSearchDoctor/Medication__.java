
package demo.acube.application.healthcare.model.calendarSearchDoctor;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medication__ {

    @SerializedName("medicine_name")
    @Expose
    private String medicineName;
    @SerializedName("dosage")
    @Expose
    private Dosage_ dosage;
    @SerializedName("images")
    @Expose
    private List<Object> images = null;

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Dosage_ getDosage() {
        return dosage;
    }

    public void setDosage(Dosage_ dosage) {
        this.dosage = dosage;
    }

    public List<Object> getImages() {
        return images;
    }

    public void setImages(List<Object> images) {
        this.images = images;
    }

}
