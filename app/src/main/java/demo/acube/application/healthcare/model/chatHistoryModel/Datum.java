
package demo.acube.application.healthcare.model.chatHistoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("message_id")
    @Expose
    private Integer messageId;
    @SerializedName("chat_id")
    @Expose
    private Integer chatId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sent_at")
    @Expose
    private Integer sentAt;
    @SerializedName("seen")
    @Expose
    private Boolean seen;
    @SerializedName("user")
    @Expose
    private User user;

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSentAt() {
        return sentAt;
    }

    public void setSentAt(Integer sentAt) {
        this.sentAt = sentAt;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
