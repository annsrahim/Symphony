
package demo.acube.application.healthcare.model.appointmentHistoryBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("appointment_id")
    @Expose
    private Integer appointmentId;
    @SerializedName("starts_at")
    @Expose
    private String startsAt;
    @SerializedName("ends_at")
    @Expose
    private String endsAt;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("creator")
    @Expose
    private Creator creator;
    @SerializedName("editor")
    @Expose
    private Editor editor;
    @SerializedName("delete_reason")
    @Expose
    private Object deleteReason;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("created_at")
    @Expose
    private Integer createdAt;
    @SerializedName("updated_at")
    @Expose
    private Integer updatedAt;
    @SerializedName("doctor")
    @Expose
    private Doctor doctor;
    @SerializedName("doctor_alert_minutes")
    @Expose
    private String doctorAlertMinutes;
    @SerializedName("patient")
    @Expose
    private Patient patient;
    @SerializedName("patient_alert_minutes")
    @Expose
    private String patientAlertMinutes;
    @SerializedName("task")
    @Expose
    private Task task;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
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

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public Object getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(Object deleteReason) {
        this.deleteReason = deleteReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getDoctorAlertMinutes() {
        return doctorAlertMinutes;
    }

    public void setDoctorAlertMinutes(String doctorAlertMinutes) {
        this.doctorAlertMinutes = doctorAlertMinutes;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getPatientAlertMinutes() {
        return patientAlertMinutes;
    }

    public void setPatientAlertMinutes(String patientAlertMinutes) {
        this.patientAlertMinutes = patientAlertMinutes;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

}
