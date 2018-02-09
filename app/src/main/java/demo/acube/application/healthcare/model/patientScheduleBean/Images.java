package demo.acube.application.healthcare.model.patientScheduleBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anns on 03/07/17.
 */

public class Images {
    @SerializedName("small")
    @Expose
    private String small;

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    @SerializedName("large")

    @Expose
    private String large;
}
