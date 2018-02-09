
package demo.acube.application.healthcare.model.taskCountModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("text")
    @Expose
    private int text;
    @SerializedName("call")
    @Expose
    private int call;
    @SerializedName("visit_virtual")
    @Expose
    private int visit_virtual;

    @SerializedName("visit_physical")
    @Expose
    private int visit_physical;

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }

    public int getCall() {
        return call;
    }

    public void setCall(int call) {
        this.call = call;
    }

    public int getVisit_virtual() {
        return visit_virtual;
    }

    public void setVisit_virtual(int visit_virtual) {
        this.visit_virtual = visit_virtual;
    }

    public int getVisit_physical() {
        return visit_physical;
    }

    public void setVisit_physical(int visit_physical) {
        this.visit_physical = visit_physical;
    }




}
