
package demo.acube.application.healthcare.model.calendarSearchDoctor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dosage_ {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("metric_type")
    @Expose
    private Integer metricType;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getMetricType() {
        return metricType;
    }

    public void setMetricType(Integer metricType) {
        this.metricType = metricType;
    }

}
