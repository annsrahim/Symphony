
package demo.acube.application.healthcare.activity.doctor.models.doctorDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Monday {

    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("breaks")
    @Expose
    private Object breaks;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Object getBreaks() {
        return breaks;
    }

    public void setBreaks(Object breaks) {
        this.breaks = breaks;
    }

}
