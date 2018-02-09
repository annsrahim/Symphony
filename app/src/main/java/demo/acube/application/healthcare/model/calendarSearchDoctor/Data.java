
package demo.acube.application.healthcare.model.calendarSearchDoctor;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("appointment")
    @Expose
    private List<Appointment_> appointment = null;
    @SerializedName("medication")
    @Expose
    private List<Medication_> medication = null;
    @SerializedName("biometric")
    @Expose
    private List<Object> biometric = null;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public List<Appointment_> getAppointment() {
        return appointment;
    }

    public void setAppointment(List<Appointment_> appointment) {
        this.appointment = appointment;
    }

    public List<Medication_> getMedication() {
        return medication;
    }

    public void setMedication(List<Medication_> medication) {
        this.medication = medication;
    }

    public List<Object> getBiometric() {
        return biometric;
    }

    public void setBiometric(List<Object> biometric) {
        this.biometric = biometric;
    }

}
