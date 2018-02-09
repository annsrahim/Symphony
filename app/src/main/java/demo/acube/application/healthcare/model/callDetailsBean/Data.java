
package demo.acube.application.healthcare.model.callDetailsBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("call_id")
    @Expose
    private Integer callId;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("tokbox")
    @Expose
    private Tokbox tokbox;
    @SerializedName("created_at")
    @Expose
    private Integer createdAt;
    @SerializedName("sender")
    @Expose
    private Sender sender;
    @SerializedName("receiver")
    @Expose
    private Receiver receiver;

    public Integer getCallId() {
        return callId;
    }

    public void setCallId(Integer callId) {
        this.callId = callId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Tokbox getTokbox() {
        return tokbox;
    }

    public void setTokbox(Tokbox tokbox) {
        this.tokbox = tokbox;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

}
