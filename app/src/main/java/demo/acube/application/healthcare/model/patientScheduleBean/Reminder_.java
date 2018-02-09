
package demo.acube.application.healthcare.model.patientScheduleBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reminder_ {

    @SerializedName("alert_id")
    @Expose
    private Integer alertId;
    @SerializedName("reminder_id")
    @Expose
    private Integer reminderId;
    @SerializedName("alerts_at")
    @Expose
    private String alertsAt;
    @SerializedName("reminder_at")
    @Expose
    private String reminderAt;
    @SerializedName("minutes")
    @Expose
    private Integer minutes;
    @SerializedName("alert")
    @Expose
    private Integer alert;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("biometric_reading")
    @Expose
    private Object biometricReading;

    public Integer getAlertId() {
        return alertId;
    }

    public void setAlertId(Integer alertId) {
        this.alertId = alertId;
    }

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
    }

    public String getAlertsAt() {
        return alertsAt;
    }

    public void setAlertsAt(String alertsAt) {
        this.alertsAt = alertsAt;
    }

    public String getReminderAt() {
        return reminderAt;
    }

    public void setReminderAt(String reminderAt) {
        this.reminderAt = reminderAt;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getAlert() {
        return alert;
    }

    public void setAlert(Integer alert) {
        this.alert = alert;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getBiometricReading() {
        return biometricReading;
    }

    public void setBiometricReading(Object biometricReading) {
        this.biometricReading = biometricReading;
    }

}
