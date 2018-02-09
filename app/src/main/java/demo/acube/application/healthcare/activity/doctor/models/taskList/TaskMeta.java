
package demo.acube.application.healthcare.activity.doctor.models.taskList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskMeta {

    @SerializedName("visit_id")
    @Expose
    private Integer visitId;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("tokbox")
    @Expose
    private Tokbox tokbox;
    @SerializedName("created_at")
    @Expose
    private Integer createdAt;
    @SerializedName("sender")
    @Expose
    private Sender sender;
    @SerializedName("receiver")
    @Expose
    private Receiver receiver;
    @SerializedName("call_id")
    @Expose
    private Integer callId;
    @SerializedName("chat_id")
    @Expose
    private Integer chatId;
    @SerializedName("last_activity_at")
    @Expose
    private Integer lastActivityAt;
    @SerializedName("last_message")
    @Expose
    private LastMessage lastMessage;
    @SerializedName("patient")
    @Expose
    private Patient patient;
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
    @SerializedName("updated_at")
    @Expose
    private Integer updatedAt;
    @SerializedName("doctor")
    @Expose
    private Doctor doctor;
    @SerializedName("doctor_alert_minutes")
    @Expose
    private String doctorAlertMinutes;
    @SerializedName("patient_alert_minutes")
    @Expose
    private String patientAlertMinutes;
    @SerializedName("task")
    @Expose
    private Task task;

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Tokbox getTokbox() {
        return tokbox;
    }

    public void setTokbox(Tokbox tokbox) {
        this.tokbox = tokbox;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Integer getCallId() {
        return callId;
    }

    public void setCallId(Integer callId) {
        this.callId = callId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(Integer lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

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
