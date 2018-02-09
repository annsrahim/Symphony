
package demo.acube.application.healthcare.model.doctorGroupSpeciality;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("specialty_id")
    @Expose
    private Integer specialtyId;
    @SerializedName("specialty_name")
    @Expose
    private String specialtyName;
    @SerializedName("doctors")
    @Expose
    private List<Doctor> doctors = null;

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

}
