package demo.acube.application.healthcare.activity.doctor.models;

/**
 * Created by Anns on 19/09/17.
 */

public class CalendarItemModel {
    private String patientName="";
    private String startsAt="";
    private int taskType;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }

    public boolean isNameAppointment() {
        return isNameAppointment;
    }

    public void setNameAppointment(boolean nameAppointment) {
        isNameAppointment = nameAppointment;
    }

    public boolean isNameReminder() {
        return isNameReminder;
    }

    public void setNameReminder(boolean nameReminder) {
        isNameReminder = nameReminder;
    }

    public boolean isAppointment() {
        return isAppointment;
    }

    public void setAppointment(boolean appointment) {
        isAppointment = appointment;
    }

    public boolean isReminder() {
        return isReminder;
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private String taskTypeName = "";
    private boolean isNameAppointment = false;
    private boolean isNameReminder = false;
    private boolean isAppointment = false;
    private boolean isReminder = false;
    private int position;

}
