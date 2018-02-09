
package demo.acube.application.healthcare.model.appointmentHistoryBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Editor {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_type")
    @Expose
    private String userType;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
