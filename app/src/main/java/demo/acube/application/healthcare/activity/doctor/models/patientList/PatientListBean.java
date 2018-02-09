
package demo.acube.application.healthcare.activity.doctor.models.patientList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientListBean {

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
