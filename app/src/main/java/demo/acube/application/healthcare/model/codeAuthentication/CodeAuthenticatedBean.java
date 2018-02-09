
package demo.acube.application.healthcare.model.codeAuthentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CodeAuthenticatedBean {

    @SerializedName("success")
    @Expose
    private Success success;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("error")
    @Expose
    private Error error;

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

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

}
