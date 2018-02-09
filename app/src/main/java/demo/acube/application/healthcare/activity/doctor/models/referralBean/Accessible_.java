
package demo.acube.application.healthcare.activity.doctor.models.referralBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Accessible_ {

    @SerializedName("call")
    @Expose
    private Boolean call;
    @SerializedName("text")
    @Expose
    private Boolean text;
    @SerializedName("visit")
    @Expose
    private Boolean visit;

    public Boolean getCall() {
        return call;
    }

    public void setCall(Boolean call) {
        this.call = call;
    }

    public Boolean getText() {
        return text;
    }

    public void setText(Boolean text) {
        this.text = text;
    }

    public Boolean getVisit() {
        return visit;
    }

    public void setVisit(Boolean visit) {
        this.visit = visit;
    }

}
