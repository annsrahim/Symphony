
package demo.acube.application.healthcare.model.callRequestBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tokbox {

    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("token")
    @Expose
    private String token;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
