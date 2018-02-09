
package demo.acube.application.healthcare.activity.doctor.models.officeHours;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wednesday {

    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("breaks")
    @Expose
    private List<Object> breaks = null;

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

    public List<Object> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<Object> breaks) {
        this.breaks = breaks;
    }

}
