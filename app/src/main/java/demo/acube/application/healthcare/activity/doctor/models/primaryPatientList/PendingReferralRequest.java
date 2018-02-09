
package demo.acube.application.healthcare.activity.doctor.models.primaryPatientList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingReferralRequest {

    @SerializedName("referral_id")
    @Expose
    private Integer referralId;
    @SerializedName("request_reason")
    @Expose
    private String requestReason;
    @SerializedName("requested_at")
    @Expose
    private String requestedAt;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("declined_reason")
    @Expose
    private Object declinedReason;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("specialist_doctor")
    @Expose
    private SpecialistDoctor specialistDoctor;

    public Integer getReferralId() {
        return referralId;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public String getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(String requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getDeclinedReason() {
        return declinedReason;
    }

    public void setDeclinedReason(Object declinedReason) {
        this.declinedReason = declinedReason;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SpecialistDoctor getSpecialistDoctor() {
        return specialistDoctor;
    }

    public void setSpecialistDoctor(SpecialistDoctor specialistDoctor) {
        this.specialistDoctor = specialistDoctor;
    }

}
