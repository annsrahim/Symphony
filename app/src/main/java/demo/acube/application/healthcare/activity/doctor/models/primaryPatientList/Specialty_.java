
package demo.acube.application.healthcare.activity.doctor.models.primaryPatientList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Specialty_ {

    @SerializedName("specialty_id")
    @Expose
    private Integer specialtyId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("primary")
    @Expose
    private Boolean primary;

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

}
