
package demo.acube.application.healthcare.model.recentChat;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecentChatBean {

    @SerializedName("success")
    @Expose
    private Success success;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
