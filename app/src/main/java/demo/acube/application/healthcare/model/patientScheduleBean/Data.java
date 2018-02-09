
package demo.acube.application.healthcare.model.patientScheduleBean;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("appointments")
    @Expose
    private List<Appointment> appointments = null;
    @SerializedName("reminders")
    @Expose
    private List<Reminder> reminders = null;

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

}
