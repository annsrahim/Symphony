
package demo.acube.application.healthcare.activity.doctor.models.officeHours;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("monday")
    @Expose
    private Monday monday;
    @SerializedName("friday")
    @Expose
    private Friday friday;
    @SerializedName("sunday")
    @Expose
    private Sunday sunday;
    @SerializedName("tuesday")
    @Expose
    private Tuesday tuesday;
    @SerializedName("thursday")
    @Expose
    private Thursday thursday;
    @SerializedName("saturday")
    @Expose
    private Saturday saturday;
    @SerializedName("wednesday")
    @Expose
    private Wednesday wednesday;

    public Monday getMonday() {
        return monday;
    }

    public void setMonday(Monday monday) {
        this.monday = monday;
    }

    public Friday getFriday() {
        return friday;
    }

    public void setFriday(Friday friday) {
        this.friday = friday;
    }

    public Sunday getSunday() {
        return sunday;
    }

    public void setSunday(Sunday sunday) {
        this.sunday = sunday;
    }

    public Tuesday getTuesday() {
        return tuesday;
    }

    public void setTuesday(Tuesday tuesday) {
        this.tuesday = tuesday;
    }

    public Thursday getThursday() {
        return thursday;
    }

    public void setThursday(Thursday thursday) {
        this.thursday = thursday;
    }

    public Saturday getSaturday() {
        return saturday;
    }

    public void setSaturday(Saturday saturday) {
        this.saturday = saturday;
    }

    public Wednesday getWednesday() {
        return wednesday;
    }

    public void setWednesday(Wednesday wednesday) {
        this.wednesday = wednesday;
    }

}
