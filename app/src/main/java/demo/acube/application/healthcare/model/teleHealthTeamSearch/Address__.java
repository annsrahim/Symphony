
package demo.acube.application.healthcare.model.teleHealthTeamSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address__ {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opus_email")
    @Expose
    private String opusEmail;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("bio")
    @Expose
    private Object bio;
    @SerializedName("dnd")
    @Expose
    private Integer dnd;
    @SerializedName("phone")
    @Expose
    private Phone__ phone;
    @SerializedName("website")
    @Expose
    private Object website;
    @SerializedName("accessible")
    @Expose
    private Accessible__ accessible;
    @SerializedName("address")
    @Expose
    private Address___ address;
    @SerializedName("specialty")
    @Expose
    private Specialty___ specialty;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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

    public Object getBio() {
        return bio;
    }

    public void setBio(Object bio) {
        this.bio = bio;
    }

    public Integer getDnd() {
        return dnd;
    }

    public void setDnd(Integer dnd) {
        this.dnd = dnd;
    }

    public Phone__ getPhone() {
        return phone;
    }

    public void setPhone(Phone__ phone) {
        this.phone = phone;
    }

    public Object getWebsite() {
        return website;
    }

    public void setWebsite(Object website) {
        this.website = website;
    }

    public Accessible__ getAccessible() {
        return accessible;
    }

    public void setAccessible(Accessible__ accessible) {
        this.accessible = accessible;
    }

    public Address___ getAddress() {
        return address;
    }

    public void setAddress(Address___ address) {
        this.address = address;
    }

    public Specialty___ getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty___ specialty) {
        this.specialty = specialty;
    }

}