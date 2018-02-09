
package demo.acube.application.healthcare.model.calendarSearchDoctor;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medication_ {

    @SerializedName("reminder_id")
    @Expose
    private Integer reminderId;
    @SerializedName("starts_at")
    @Expose
    private String startsAt;
    @SerializedName("ends_at")
    @Expose
    private String endsAt;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("times_per_day")
    @Expose
    private Integer timesPerDay;
    @SerializedName("patient")
    @Expose
    private Patient___ patient;
    @SerializedName("doctor")
    @Expose
    private Doctor___ doctor;
    @SerializedName("creator")
    @Expose
    private Creator___ creator;
    @SerializedName("editor")
    @Expose
    private Editor___ editor;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("times")
    @Expose
    private List<String> times = null;
    @SerializedName("alert_minutes")
    @Expose
    private String alertMinutes;
    @SerializedName("delete_reason")
    @Expose
    private Object deleteReason;
    @SerializedName("created_at")
    @Expose
    private Integer createdAt;
    @SerializedName("updated_at")
    @Expose
    private Integer updatedAt;
    @SerializedName("medication")
    @Expose
    private Medication__ medication;
    @SerializedName("reminders")
    @Expose
    private List<Object> reminders = null;

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public String getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(String endsAt) {
        this.endsAt = endsAt;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTimesPerDay() {
        return timesPerDay;
    }

    public void setTimesPerDay(Integer timesPerDay) {
        this.timesPerDay = timesPerDay;
    }

    public Patient___ getPatient() {
        return patient;
    }

    public void setPatient(Patient___ patient) {
        this.patient = patient;
    }

    public Doctor___ getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor___ doctor) {
        this.doctor = doctor;
    }

    public Creator___ getCreator() {
        return creator;
    }

    public void setCreator(Creator___ creator) {
        this.creator = creator;
    }

    public Editor___ getEditor() {
        return editor;
    }

    public void setEditor(Editor___ editor) {
        this.editor = editor;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public String getAlertMinutes() {
        return alertMinutes;
    }

    public void setAlertMinutes(String alertMinutes) {
        this.alertMinutes = alertMinutes;
    }

    public Object getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(Object deleteReason) {
        this.deleteReason = deleteReason;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Integer updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Medication__ getMedication() {
        return medication;
    }

    public void setMedication(Medication__ medication) {
        this.medication = medication;
    }

    public List<Object> getReminders() {
        return reminders;
    }

    public void setReminders(List<Object> reminders) {
        this.reminders = reminders;
    }

}
