
package demo.acube.application.healthcare.model.calendarSearchDoctor;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Name {

    @SerializedName("appointment")
    @Expose
    private List<Appointment> appointment = null;
    @SerializedName("reminder")
    @Expose
    private List<Reminder> reminder = null;

    public List<Appointment> getAppointment() {
        return appointment;
    }

    public void setAppointment(List<Appointment> appointment) {
        this.appointment = appointment;
    }

    public List<Reminder> getReminder() {
        return reminder;
    }

    public void setReminder(List<Reminder> reminder) {
        this.reminder = reminder;
    }

}
