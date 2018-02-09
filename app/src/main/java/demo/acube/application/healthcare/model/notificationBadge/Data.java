
package demo.acube.application.healthcare.model.notificationBadge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("call")
    @Expose
    private Integer call;
    @SerializedName("visit")
    @Expose
    private Integer visit;
    @SerializedName("text")
    @Expose
    private Integer text;
    @SerializedName("appointment")
    @Expose
    private Integer appointment;
    @SerializedName("reminder")
    @Expose
    private Integer reminder;
    @SerializedName("referral")
    @Expose
    private Integer referral;
    @SerializedName("patient")
    @Expose
    private Integer patient;

    public Integer getCall() {
        return call;
    }

    public void setCall(Integer call) {
        this.call = call;
    }

    public Integer getVisit() {
        return visit;
    }

    public void setVisit(Integer visit) {
        this.visit = visit;
    }

    public Integer getText() {
        return text;
    }

    public void setText(Integer text) {
        this.text = text;
    }

    public Integer getAppointment() {
        return appointment;
    }

    public void setAppointment(Integer appointment) {
        this.appointment = appointment;
    }

    public Integer getReminder() {
        return reminder;
    }

    public void setReminder(Integer reminder) {
        this.reminder = reminder;
    }

    public Integer getReferral() {
        return referral;
    }

    public void setReferral(Integer referral) {
        this.referral = referral;
    }

    public Integer getPatient() {
        return patient;
    }

    public void setPatient(Integer patient) {
        this.patient = patient;
    }

}
