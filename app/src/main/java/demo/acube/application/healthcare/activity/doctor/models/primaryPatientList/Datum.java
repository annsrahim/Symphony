
package demo.acube.application.healthcare.activity.doctor.models.primaryPatientList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opus_email")
    @Expose
    private String opusEmail;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("primary_doctor")
    @Expose
    private PrimaryDoctor primaryDoctor;
    @SerializedName("pending_referral_requests")
    @Expose
    private List<PendingReferralRequest> pendingReferralRequests = null;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpusEmail() {
        return opusEmail;
    }

    public void setOpusEmail(String opusEmail) {
        this.opusEmail = opusEmail;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public PrimaryDoctor getPrimaryDoctor() {
        return primaryDoctor;
    }

    public void setPrimaryDoctor(PrimaryDoctor primaryDoctor) {
        this.primaryDoctor = primaryDoctor;
    }

    public List<PendingReferralRequest> getPendingReferralRequests() {
        return pendingReferralRequests;
    }

    public void setPendingReferralRequests(List<PendingReferralRequest> pendingReferralRequests) {
        this.pendingReferralRequests = pendingReferralRequests;
    }

}
