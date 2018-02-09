
package demo.acube.application.healthcare.model.patientScheduleBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("device_id")
    @Expose
    private Integer deviceId;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
