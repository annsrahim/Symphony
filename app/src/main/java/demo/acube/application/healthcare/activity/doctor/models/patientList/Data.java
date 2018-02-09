
package demo.acube.application.healthcare.activity.doctor.models.patientList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("primary_patients")
    @Expose
    private List<PrimaryPatient> primaryPatients = null;
    @SerializedName("secondary_patients")
    @Expose
    private List<SecondaryPatient> secondaryPatients = null;

    public List<PrimaryPatient> getPrimaryPatients() {
        return primaryPatients;
    }

    public void setPrimaryPatients(List<PrimaryPatient> primaryPatients) {
        this.primaryPatients = primaryPatients;
    }

    public List<SecondaryPatient> getSecondaryPatients() {
        return secondaryPatients;
    }

    public void setSecondaryPatients(List<SecondaryPatient> secondaryPatients) {
        this.secondaryPatients = secondaryPatients;
    }

}
