
package demo.acube.application.healthcare.model.patientScheduleBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientScheduleBean {

    @SerializedName("success")
    @Expose
    private Success success;
    @SerializedName("data")
    @Expose
    private Data data;

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
