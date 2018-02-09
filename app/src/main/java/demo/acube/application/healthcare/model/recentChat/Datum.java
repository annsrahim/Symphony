
package demo.acube.application.healthcare.model.recentChat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("chat_id")
    @Expose
    private Integer chatId;
    @SerializedName("created_at")
    @Expose
    private Integer createdAt;
    @SerializedName("last_activity_at")
    @Expose
    private Integer lastActivityAt;
    @SerializedName("patient")
    @Expose
    private Patient patient;
    @SerializedName("doctor")
    @Expose
    private Doctor doctor;
    @SerializedName("last_message")
    @Expose
    private LastMessage lastMessage;

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(Integer lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

}
